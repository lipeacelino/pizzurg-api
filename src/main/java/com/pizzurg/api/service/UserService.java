package com.pizzurg.api.service;

import com.pizzurg.api.config.exception.EmailExistsException;
import com.pizzurg.api.config.security.SecurityConfiguration;
import com.pizzurg.api.config.security.TokenJwtService;
import com.pizzurg.api.config.security.UserDetailsImpl;
import com.pizzurg.api.dto.auth.TokenJwtDto;
import com.pizzurg.api.dto.user.LoginUserDto;
import com.pizzurg.api.dto.user.RegisterUserDto;
import com.pizzurg.api.entity.Role;
import com.pizzurg.api.entity.User;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.repository.RoleRepository;
import com.pizzurg.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SecurityConfiguration securityConfiguration;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenJwtService tokenJwtService;

    //depois ver se tem necessidade de fazer um try/catch nesse fluxo de autenticação
    public TokenJwtDto authenticateUser(LoginUserDto loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());
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
                    .roles(List.of(getRole(RoleName.ROLE_CLIENT)))
                    .build();
            userRepository.save(newUser);
        }
    }

    private boolean checkIfEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistsException();
        }
        return true;
    }
    private Role getRole(RoleName roleName) {
       Optional<Role> roleOptional = roleRepository.findByName(roleName.name());
       if (roleOptional.isPresent()) {
           return roleOptional.get();
       }
       Role role = Role.builder().name(roleName).build();
       return roleRepository.save(role);
    }
}
