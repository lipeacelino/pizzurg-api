package com.pizzurg.api.services;

import com.pizzurg.api.dto.output.user.RecoveryUserDto;
import com.pizzurg.api.exception.EmailExistsException;
import com.pizzurg.api.exception.UserAssociatedWithOrder;
import com.pizzurg.api.exception.UserNotFoundException;
import com.pizzurg.api.mappers.UserMapper;
import com.pizzurg.api.repositories.OrderRepository;
import com.pizzurg.api.security.SecurityConfiguration;
import com.pizzurg.api.security.TokenJwtService;
import com.pizzurg.api.security.UserDetailsImpl;
import com.pizzurg.api.dto.output.auth.TokenJwtDto;
import com.pizzurg.api.dto.input.user.LoginUserDto;
import com.pizzurg.api.dto.input.user.CreateUserDto;
import com.pizzurg.api.entities.User;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserMapper userMapper;

    public TokenJwtDto authenticateUser(LoginUserDto loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new TokenJwtDto(tokenJwtService.generateToken(userDetails));
    }

    public void createUser(CreateUserDto createUserDto, RoleName roleName) {
        if (checkIfEmailNotExists(createUserDto.email())) {
            User newUser = User.builder()
                    .email(createUserDto.email())
                    .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                    .roleList(List.of(roleService.getRole(roleName)))
                    .build();
            userRepository.save(newUser);
        }
    }

    public Page<RecoveryUserDto> recoveryUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(user -> userMapper.mapUserToUserDto(user));
    }

    public RecoveryUserDto recoveryUserById(Long userId) {
        return userMapper.mapUserToUserDto(userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));
    }

    private boolean checkIfEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistsException();
        }
        return true;
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        if(orderRepository.findFirstByUserId(userId).isPresent()) {
            throw new UserAssociatedWithOrder();
        }
        userRepository.deleteById(userId);
    }

}
