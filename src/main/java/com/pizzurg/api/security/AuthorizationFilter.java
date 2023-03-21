package com.pizzurg.api.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzurg.api.entity.User;
import com.pizzurg.api.exception.MissingTokenException;
import com.pizzurg.api.exception.handler.ApiError;
import com.pizzurg.api.repository.UserRepository;
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
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenJwtService tokenJwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (checkIfEndpointIsNotPublic(request)) {
                String token = recoveryToken(request);
                if (token != null) {
                    String subject = tokenJwtService.getSubjectFromToken(token);
                    User user = userRepository.findByEmail(subject).get();
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUser().getEmail(), null, userDetails.getAuthorities()); //acho que dá pra trocar por um new SimpleAuthority...
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new MissingTokenException();
                }
            }
            filterChain.doFilter(request, response);
        } catch (MissingTokenException ex) {
            buidErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.name(), ex, response);
        } catch (JWTCreationException ex) {
            buidErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), ex, response);
        } catch (JWTVerificationException ex) {
            buidErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), ex, response);
        }
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.PUBLIC_ENDPOINTS).contains(requestURI);
    }

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

    private String convertObjToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }
}
