package com.pizzurg.api.controller;

import com.pizzurg.api.dto.input.order.CreateOrderDto;
import com.pizzurg.api.dto.output.order.RecoveryOrderDto;
import com.pizzurg.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<RecoveryOrderDto> createOrder(@RequestHeader("Authorization") String token,
                                                        @RequestBody CreateOrderDto createOrderDto) {
        return new ResponseEntity<>(orderService.createOrder(token, createOrderDto), HttpStatus.OK);
    }

}
