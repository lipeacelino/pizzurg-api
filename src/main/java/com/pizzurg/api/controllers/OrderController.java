package com.pizzurg.api.controllers;

import com.pizzurg.api.dto.input.order.ChangeStatusOrderDto;
import com.pizzurg.api.dto.input.order.CreateOrderDto;
import com.pizzurg.api.dto.output.order.RecoveryOrderDto;
import com.pizzurg.api.services.OrderService;
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
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<RecoveryOrderDto> createOrder(@RequestHeader("Authorization") String token,
                                                        @RequestBody CreateOrderDto createOrderDto) {
        return new ResponseEntity<>(orderService.createOrder(token, createOrderDto), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<RecoveryOrderDto> getOrder(@RequestHeader("Authorization") String token,
                                                     @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrder(token, orderId), HttpStatus.OK);
    }

    @GetMapping("/status/{statusName}")
    public ResponseEntity<Page<RecoveryOrderDto>> getOrderByStatus(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @PathVariable String statusName,
            @RequestHeader("Authorization") String token
    ) {
        return new ResponseEntity<>(orderService.getOrderByStatus(statusName, token, pageable), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<RecoveryOrderDto> changeOrderStatus(@PathVariable Long orderId,
                                                              @RequestBody ChangeStatusOrderDto changeStatusOrderDto) {
        return new ResponseEntity<>(orderService.changeOrderStatus(orderId, changeStatusOrderDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<RecoveryOrderDto>> getAllOrders(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @RequestHeader("Authorization") String token
    ) {
        return new ResponseEntity<>(orderService.getAllOrders(token, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long orderId) {
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}