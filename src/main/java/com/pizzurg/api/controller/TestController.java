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
        return new ResponseEntity("Authorization test", HttpStatus.OK);
    }

    @GetMapping("/customer")
    public ResponseEntity test2() {
        return new ResponseEntity("Customer test authorization", HttpStatus.OK);
    }

    @GetMapping("/employee")
    public ResponseEntity test3() {
        return new ResponseEntity("Employee test authorization", HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity test4() {
        return new ResponseEntity("Administrator test authorization", HttpStatus.OK);
    }

}
