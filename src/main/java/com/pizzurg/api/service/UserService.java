package com.pizzurg.api.service;

import com.pizzurg.api.config.exception.EmailExistsException;
import com.pizzurg.api.config.security.SecurityConfigurations;
import com.pizzurg.api.dto.user.LoginUser;
import com.pizzurg.api.dto.user.RegisterUserDto;
import com.pizzurg.api.entity.User;
import com.pizzurg.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityConfigurations securityConfigurations;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void authenticateUser(LoginUser loginUser) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.email(), loginUser.password());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Transactional
    public void registerNewUser(RegisterUserDto registerUserDto) {
        if (checkIfEmailNotExists(registerUserDto.email())) {
            User newUser = User.builder()
                            .email(registerUserDto.email())
                            .password(securityConfigurations.passwordEncoder().encode(registerUserDto.password()))
                            .build();
            userRepository.save(newUser);
        }
    }

    private boolean checkIfEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistsException();
        };
        return true;
    }

//    private boolean checkIfUserEmailExists(String email) {
//        if (userRepository.findByEmail(email).isEmpty()) {
//            throw new UserNotFounException();
//        }
//        return true;
//    }
}
