package com.pizzurg.api.service;

import com.pizzurg.api.config.exception.EmailExistsException;
import com.pizzurg.api.config.security.SecurityConfiguration;
import com.pizzurg.api.config.security.TokenJwtService;
import com.pizzurg.api.config.security.UserDetailsImpl;
import com.pizzurg.api.dto.auth.TokenJwtDto;
import com.pizzurg.api.dto.user.LoginUser;
import com.pizzurg.api.dto.user.RegisterUserDto;
import com.pizzurg.api.entity.User;
import com.pizzurg.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityConfiguration securityConfiguration;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenJwtService tokenJwtService;

    //depois ver se tem necessidade de fazer um try/catch nesse fluxo de autenticação
    public TokenJwtDto authenticateUser(LoginUser loginUser) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.email(), loginUser.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new TokenJwtDto(tokenJwtService.generateToken(userDetails));
    }

    @Transactional
    public void registerNewUser(RegisterUserDto registerUserDto) {
        if (checkIfEmailNotExists(registerUserDto.email())) {
            User newUser = User.builder()
                    .email(registerUserDto.email())
                    .password(securityConfiguration.passwordEncoder().encode(registerUserDto.password()))
                    .build();
            userRepository.save(newUser);
        }
    }

    private boolean checkIfEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistsException();
        }
        ;
        return true;
    }

//    private boolean checkIfUserEmailExists(String email) {
//        if (userRepository.findByEmail(email).isEmpty()) {
//            throw new UserNotFounException();
//        }
//        return true;
//    }
}
