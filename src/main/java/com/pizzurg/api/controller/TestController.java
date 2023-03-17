package com.pizzurg.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity test() {
        return new ResponseEntity("Teste de autorização", HttpStatus.OK);
    }

    @GetMapping("/client")
    public ResponseEntity test2() {
        return new ResponseEntity("Teste de autorização do cliente", HttpStatus.OK);
    }

    @GetMapping("/employee")
    public ResponseEntity test3() {
        return new ResponseEntity("Teste de autorização do funcionário", HttpStatus.OK);
    }

}
