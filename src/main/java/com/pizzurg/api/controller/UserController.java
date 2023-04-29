package com.pizzurg.api.controller;

import com.pizzurg.api.dto.input.user.CreateUserDto;
import com.pizzurg.api.dto.input.user.LoginUserDto;
import com.pizzurg.api.dto.output.auth.TokenJwtDto;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenJwtDto> authenticateUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        TokenJwtDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
    @PostMapping("/customers")
    public ResponseEntity<Void> createCustomerUser(@Valid @RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto, RoleName.ROLE_CUSTOMER);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
