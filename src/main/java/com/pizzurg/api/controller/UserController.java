package com.pizzurg.api.controller;

import com.pizzurg.api.dto.auth.TokenJwtDto;
import com.pizzurg.api.dto.user.LoginUserDto;
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

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody LoginUserDto loginUserDto){
        TokenJwtDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity(token, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity registerUser(@RequestBody RegisterUserDto registerUserDto, UriComponentsBuilder uriBuilder) {
        userService.registerNewUser(registerUserDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
