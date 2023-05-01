package com.pizzurg.api.security.authentication;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzurg.api.entities.User;
import com.pizzurg.api.exception.MissingTokenException;
import com.pizzurg.api.exception.model.ApiError;
import com.pizzurg.api.repositories.UserRepository;
import com.pizzurg.api.security.config.SecurityConfiguration;
import com.pizzurg.api.security.userdetails.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Verifica se o endpoint requer autenticação antes de processar a requisição
            if (checkIfEndpointIsNotPublic(request)) {
                String token = recoveryToken(request); // Recupera o token do cabeçalho Authorization da requisição
                if (token != null) {
                    String subject = jwtTokenService.getSubjectFromToken(token); // Obtém o assunto (neste caso, o nome de usuário) do token
                    User user = userRepository.findByEmail(subject).get(); // Busca o usuário pelo email (que é o assunto do token)
                    UserDetailsImpl userDetails = new UserDetailsImpl(user); // Cria um UserDetails com o usuário encontrado

                    // Cria um objeto de autenticação do Spring Security
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUser().getEmail(), null, userDetails.getAuthorities());

                    // Define o objeto de autenticação no contexto de segurança do Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new MissingTokenException();
                }
            }
            filterChain.doFilter(request, response); // Continua o processamento da requisição
        } catch (MissingTokenException ex) {
            buidErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.name(), ex, response);
        } catch (JWTCreationException ex) {
            buidErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), ex, response);
        } catch (JWTVerificationException ex) {
            buidErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), ex, response);
        }
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }

    // Constroi e envia uma resposta de erro JSON em caso de exceção
    private void buidErrorResponse(Integer codeError, String statusError, Exception ex, HttpServletResponse response) throws IOException {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(codeError)
                .status(statusError)
                .errors(List.of(ex.getMessage()))
                .build();
        response.setStatus(codeError);
        response.setContentType("application/json");
        response.getWriter().write(convertObjToJson(apiError));
    }

    // Converte um objeto Java em formato JSON
    private String convertObjToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

}
