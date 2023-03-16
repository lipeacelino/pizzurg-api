package com.pizzurg.api.controller;

import com.pizzurg.api.dto.user.LoginUser;
import com.pizzurg.api.dto.user.RegisterUserDto;
import com.pizzurg.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/login")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody LoginUser loginUser){
        String tokenJwt = userService.authenticateUser(loginUser);
        return new ResponseEntity(tokenJwt, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity registerUser(@RequestBody RegisterUserDto registerUserDto, UriComponentsBuilder uriBuilder) {
        userService.registerNewUser(registerUserDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
