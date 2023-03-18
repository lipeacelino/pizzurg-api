package com.pizzurg.api.service;

import com.pizzurg.api.exception.EmailExistsException;
import com.pizzurg.api.security.SecurityConfiguration;
import com.pizzurg.api.security.TokenJwtService;
import com.pizzurg.api.security.UserDetailsImpl;
import com.pizzurg.api.dto.auth.TokenJwtDto;
import com.pizzurg.api.dto.user.LoginUserDto;
import com.pizzurg.api.dto.user.CreateUserDto;
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
    private RoleService roleService;
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
    public void createUser(CreateUserDto createUserDto, RoleName roleName) {
        if (checkIfEmailNotExists(createUserDto.email())) {
            User newUser = User.builder()
                    .email(createUserDto.email())
                    .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                    .roles(List.of(roleService.getRole(roleName)))
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
}
