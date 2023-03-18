package com.pizzurg.api.config;

import com.pizzurg.api.dto.user.CreateUserDto;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.repository.UserRepository;
import com.pizzurg.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "12345";


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            createAdminUser();
        } catch(Exception e) {
            throw new Exception("Error creating admin user", e);
        }
    }
    private void createAdminUser() {
        if (userRepository.findByEmail(EMAIL).isEmpty()) {
            userService.createUser(new CreateUserDto(EMAIL, PASSWORD), RoleName.ROLE_ADMINISTRATOR);
        }
    }

}
