package com.pizzurg.api.controllers;

import com.pizzurg.api.dto.auth.TokenJwtDto;
import com.pizzurg.api.dto.user.LoginUserDto;
import com.pizzurg.api.dto.user.CreateUserDto;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginUserDto loginUserDto) {
        TokenJwtDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity(token, HttpStatus.OK);
    }

    @PostMapping("/new/customer")
    public ResponseEntity createCustomerUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto, RoleName.ROLE_CUSTOMER);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/new/employee")
    public ResponseEntity createEmployeeUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto, RoleName.ROLE_EMPLOYEE);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
