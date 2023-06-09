package com.pizzurg.api.services;

import com.pizzurg.api.dto.output.user.RecoveryUserDto;
import com.pizzurg.api.entities.Role;
import com.pizzurg.api.exception.EmailAlreadyExistsException;
import com.pizzurg.api.exception.UserAssociatedWithOrderException;
import com.pizzurg.api.exception.UserNotFoundException;
import com.pizzurg.api.mappers.UserMapper;
import com.pizzurg.api.repositories.OrderRepository;
import com.pizzurg.api.repositories.RoleRepository;
import com.pizzurg.api.security.config.SecurityConfiguration;
import com.pizzurg.api.security.authentication.JwtTokenService;
import com.pizzurg.api.security.userdetails.UserDetailsImpl;
import com.pizzurg.api.dto.output.auth.RecoveryJwtTokenDto;
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
    private JwtTokenService jwtTokenService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserMapper userMapper;

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public void createUser(CreateUserDto createUserDto, RoleName roleName) {
        // Verifica se o e-mail fornecido não existe no banco de dados
        if (checkIfEmailNotExists(createUserDto.email())) {

            // Cria um novo usuário com os dados fornecidos
            User newUser = User.builder()
                    .email(createUserDto.email())
                    // Codifica a senha do usuário com o algoritmo bcrypt
                    .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                    // Atribui ao usuário uma permissão específica
                    .roles(List.of(getRole(roleName)))
                    .build();

            // Salva o novo usuário no banco de dados
            userRepository.save(newUser);
        }
    }

    public Page<RecoveryUserDto> getUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(user -> userMapper.mapUserToUserDto(user));
    }

    public RecoveryUserDto getUserById(Long userId) {
        return userMapper.mapUserToUserDto(userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        // Se um usuário estiver associado a um pedido, o usuário não pode ser excluído
        if(orderRepository.findFirstByUserId(userId).isPresent()) {
            throw new UserAssociatedWithOrderException();
        }
        userRepository.deleteById(userId);
    }

    private boolean checkIfEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        return true;
    }

    private Role getRole(RoleName roleName) {
        Optional<Role> roleOptional= roleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        Role role = Role.builder().name(roleName).build();
        return roleRepository.save(role);
    }

}
