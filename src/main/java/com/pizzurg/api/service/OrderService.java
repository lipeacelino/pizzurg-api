package com.pizzurg.api.service;

import com.pizzurg.api.dto.input.order.ChangeStatusOrderDto;
import com.pizzurg.api.dto.input.order.CreateOrderDto;
import com.pizzurg.api.dto.input.order.CreateOrderItemDto;
import com.pizzurg.api.dto.output.order.RecoveryOrderDto;
import com.pizzurg.api.entity.*;
import com.pizzurg.api.enums.MethodPayment;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.enums.Status;
import com.pizzurg.api.exception.OrderNotFoundByUserException;
import com.pizzurg.api.exception.OrderNotFoundException;
import com.pizzurg.api.exception.ProductVariationNotFoundException;
import com.pizzurg.api.exception.UserNotFoundException;
import com.pizzurg.api.mapper.OrderMapper;
import com.pizzurg.api.repository.OrderRepository;
import com.pizzurg.api.repository.ProductVariationRepository;
import com.pizzurg.api.repository.UserRepository;
import com.pizzurg.api.security.TokenJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                    .orElseThrow(ProductVariationNotFoundException::new);

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
        User user = getUser(token);

        //monta o order com todos os itens das ordens
        Order order = Order.builder()
                .user(user)
                .methodPayment(MethodPayment.valueOf(createOrderDto.methodPayment().toUpperCase()))
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

    public RecoveryOrderDto getOrder(String token, Long orderId) {
        User user = getUser(token);

        //se tiver role de admin deve mostrar qualquer pedido, se não, mostrar o pedido apenas se ele pertencer ao usuário atual
        if (user.getRoleList().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            return orderMapper.mapOrderToRecoveryOrderDto(orderRepository
                    .findByUserIdAndOrderId(orderId, user.getId())
                    .orElseThrow(OrderNotFoundByUserException::new));
        }
        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new));
    }

    public Page<RecoveryOrderDto> getAllOrders(String token, Pageable pageable) {
        User user = getUser(token);

        //se tiver role de admin deve mostrar tudo, se não, mostrar apenas os pedidos que pertencem ao usuário atual
        if (user.getRoleList().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            Page<Order> orderPage = orderRepository.findAllByUserId(user.getId(), pageable);
            return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
        }
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
    }

    public Page<RecoveryOrderDto> getOrderByStatus(String statusName, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByStatus(Status.valueOf(statusName.toUpperCase()), pageable);
        return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
    }

    private User getUser(String token) {
        String subject = tokenJwtService.getSubjectFromToken(token.replace("Bearer ", ""));
        return userRepository.findByEmail(subject).orElseThrow(UserNotFoundException::new);
    }

    public RecoveryOrderDto changeOrderStatus(Long orderId, ChangeStatusOrderDto changeStatusOrderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundByUserException::new);
        order.setStatus(Status.valueOf(changeStatusOrderDto.status().toUpperCase()));
        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }

}
