package com.pizzurg.api.service;

import com.pizzurg.api.dto.input.order.CreateOrderDto;
import com.pizzurg.api.dto.input.order.CreateOrderItemDto;
import com.pizzurg.api.dto.output.order.RecoveryOrderDto;
import com.pizzurg.api.entity.*;
import com.pizzurg.api.exception.UserNotFoundException;
import com.pizzurg.api.mapper.OrderMapper;
import com.pizzurg.api.repository.OrderRepository;
import com.pizzurg.api.repository.ProductVariationRepository;
import com.pizzurg.api.repository.UserRepository;
import com.pizzurg.api.security.TokenJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private TokenJwtService tokenJwtService;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMapper orderMapper;

    public RecoveryOrderDto createOrder(String token, CreateOrderDto createOrderDto) {
        List<OrderItem> orderItemList = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;

        for(CreateOrderItemDto createOrderItemDto : createOrderDto.orderItems()) {
            //verifica se a variação de produto existe
            ProductVariation productVariation = productVariationRepository
                    .findByIdAndProductId(createOrderItemDto.productVariationId(), createOrderItemDto.productId())
                    .orElseThrow(() -> new RuntimeException("ProductVariation não encontrado."));

            //multiplica a quantidade de itens pelo preço e adiciona ao valor total
            amount = amount.add(productVariation.getPrice().multiply(new BigDecimal(createOrderItemDto.quantity())));

            //cria o item da ordem e adiciona a lista
            OrderItem orderItem = OrderItem.builder()
                    .quantity(createOrderItemDto.quantity())
                    .productVariation(productVariation)
                    .build();

            orderItemList.add(orderItem);
        }

        //monta o delivery data
        DeliveryData deliveryData = DeliveryData.builder()
                .receiverName(createOrderDto.deliveryData().receiverName())
                .address(createOrderDto.deliveryData().address())
                .number(createOrderDto.deliveryData().number())
                .complement(createOrderDto.deliveryData().complement())
                .district(createOrderDto.deliveryData().district())
                .zipCode(createOrderDto.deliveryData().zipCode())
                .city(createOrderDto.deliveryData().city())
                .state(createOrderDto.deliveryData().state())
                .build();

        //obtém usuário através do token
        String subject = tokenJwtService.getSubjectFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(subject).orElseThrow(UserNotFoundException::new);

        //monta o order com todos os itens das ordens
        Order order = Order.builder()
                .user(user)
                .methodPayment(createOrderDto.methodPayment())
                .orderItemList(orderItemList)
                .amount(amount)
                .deliveryData(deliveryData)
                .build();

        //associa cada item à ordem
        orderItemList.forEach(orderItem -> orderItem.setOrder(order));

        //associa a ordem aos dados de entrega
        deliveryData.setOrder(order);

        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }
}
