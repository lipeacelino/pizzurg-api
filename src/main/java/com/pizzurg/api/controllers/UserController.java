package com.pizzurg.api.controllers;

import com.pizzurg.api.dto.input.user.CreateUserDto;
import com.pizzurg.api.dto.input.user.LoginUserDto;
import com.pizzurg.api.dto.output.auth.RecoveryJwtTokenDto;
import com.pizzurg.api.dto.output.user.RecoveryUserDto;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
    @PostMapping("/customers")
    public ResponseEntity<Void> createCustomerUser(@Valid @RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto, RoleName.ROLE_CUSTOMER);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<RecoveryUserDto>> recoveryUsers(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable) {
        return new ResponseEntity<>(userService.recoveryUsers(pageable), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RecoveryUserDto> recoveryUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.recoveryUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
