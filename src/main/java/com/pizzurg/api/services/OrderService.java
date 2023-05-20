package com.pizzurg.api.services;

import com.pizzurg.api.dto.input.order.UpdateStatusOrderDto;
import com.pizzurg.api.dto.input.order.CreateOrderDto;
import com.pizzurg.api.dto.input.order.CreateOrderItemDto;
import com.pizzurg.api.dto.output.order.RecoveryOrderDto;
import com.pizzurg.api.entities.*;
import com.pizzurg.api.enums.PaymentMethod;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.enums.Status;
import com.pizzurg.api.exception.OrderNotFoundForUserException;
import com.pizzurg.api.exception.OrderNotFoundException;
import com.pizzurg.api.exception.ProductVariationNotFoundException;
import com.pizzurg.api.exception.UserNotFoundException;
import com.pizzurg.api.mappers.OrderMapper;
import com.pizzurg.api.repositories.OrderRepository;
import com.pizzurg.api.repositories.ProductVariationRepository;
import com.pizzurg.api.repositories.UserRepository;
import com.pizzurg.api.security.authentication.JwtTokenService;
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
    private JwtTokenService jwtTokenService;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMapper orderMapper;

    public RecoveryOrderDto createOrder(String token, CreateOrderDto createOrderDto) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;

        // Loop através de cada item do pedido recebido
        for(CreateOrderItemDto createOrderItemDto : createOrderDto.orderItems()) {
            // Verifica se a variação de produto existe
            ProductVariation productVariation = productVariationRepository
                    .findByProductIdAndProductVariationId(createOrderItemDto.productId(), createOrderItemDto.productVariationId())
                    .orElseThrow(ProductVariationNotFoundException::new);

            // Multiplica a quantidade de itens pelo preço e adiciona ao valor total
            amount = amount.add(productVariation.getPrice().multiply(new BigDecimal(createOrderItemDto.quantity())));

            // Cria um item através dos dados do DTO
            OrderItem orderItem = OrderItem.builder()
                    .quantity(createOrderItemDto.quantity())
                    .productVariation(productVariation)
                    .build();

            // Adiciona o item criado a lista de itens
            orderItems.add(orderItem);
        }

        // Cria os dados de entrega através dos dados do DTO
        DeliveryData deliveryData = DeliveryData.builder()
                .receiverName(createOrderDto.deliveryData().receiverName())
                .address(createOrderDto.deliveryData().address())
                .number(createOrderDto.deliveryData().number())
                .complement(createOrderDto.deliveryData().complement())
                .district(createOrderDto.deliveryData().district())
                .zipCode(createOrderDto.deliveryData().zipCode())
                .city(createOrderDto.deliveryData().city())
                .state(createOrderDto.deliveryData().state())
                .phoneNumber(createOrderDto.deliveryData().phone_number())
                .build();

        // Obtém usuário através do token
        User user = getUser(token);

        // Cria o pedido com todos os itens associados a ele
        Order order = Order.builder()
                .user(user)
                .paymentMethod(PaymentMethod.valueOf(createOrderDto.paymentMethod().toUpperCase()))
                .orderItems(orderItems)
                .amount(amount)
                .deliveryData(deliveryData)
                .build();

        // Associa cada item ao pedido
        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        // Associa pedido aos dados de entrega
        deliveryData.setOrder(order);

        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }

    public RecoveryOrderDto getOrderById(String token, Long orderId) {
        User user = getUser(token);

        // Se tiver role de customer deve retornar apena o pedido que pertence ao usuário atual
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            return orderMapper.mapOrderToRecoveryOrderDto(orderRepository
                    .findByOrderIdAndUserId(orderId, user.getId())
                    .orElseThrow(OrderNotFoundForUserException::new));
        }
        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new));
    }

    public Page<RecoveryOrderDto> getOrders(String token, Pageable pageable) {
        User user = getUser(token);

        // Se tiver role de customer deve retornar apenas os pedidos que pertencem ao usuário atual
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            Page<Order> orderPage = orderRepository.findAllByUserId(user.getId(), pageable);
            return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
        }
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
    }

    public Page<RecoveryOrderDto> getOrderByStatus(String statusName, String token, Pageable pageable) {
        User user = getUser(token);

        // Se tiver role de customer deve retornar apenas os pedidos que pertencem ao usuário atual.
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            Page<Order> orderPage = orderRepository.findOrderByStatusAndUser(Status.valueOf(statusName.toUpperCase()), user.getId(), pageable);
            return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
        }
        Page<Order> orderPage = orderRepository.findByStatus(Status.valueOf(statusName.toUpperCase()), pageable);
        return orderPage.map(order -> orderMapper.mapOrderToRecoveryOrderDto(order));
    }


    public RecoveryOrderDto changeOrderStatus(Long orderId, UpdateStatusOrderDto updateStatusOrderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundForUserException::new);
        order.setStatus(Status.valueOf(updateStatusOrderDto.status().toUpperCase()));
        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }

    public void deleteOrderById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException();
        }
        orderRepository.deleteById(orderId);
    }

    private User getUser(String token) {
        String subject = jwtTokenService.getSubjectFromToken(token.replace("Bearer ", ""));
        return userRepository.findByEmail(subject).orElseThrow(UserNotFoundException::new);
    }

}
