package com.pizzurg.api.mapper;

import com.pizzurg.api.dto.output.order.RecoveryDeliveryData;
import com.pizzurg.api.dto.output.order.RecoveryOrderDto;
import com.pizzurg.api.dto.output.order.RecoveryOrderItemDto;
import com.pizzurg.api.entity.DeliveryData;
import com.pizzurg.api.entity.Order;
import com.pizzurg.api.entity.OrderItem;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface OrderMapper {

    @IterableMapping(qualifiedByName = "mapOrderToRecoveryOrderDto")
    List<RecoveryOrderDto> mapOrderListToRecoveryOrderDtoList(List<Order> order);

    @Mapping(qualifiedByName = "mapDeliveryDataToRecoveryDeliveryData", target = "deliveryData")
    @Mapping(qualifiedByName = "mapOrderItemListToRecoveryOrderItemDtoList", source = "orderItemList", target = "orderItems")
    @Mapping(qualifiedByName = "mapUserToUserDto", target = "user")
    @Named("mapOrderToRecoveryOrderDto")
    RecoveryOrderDto mapOrderToRecoveryOrderDto(Order order);

    @IterableMapping(qualifiedByName = "mapOrderItemToRecoveryOrderItemDto")
    @Named("mapOrderItemListToRecoveryOrderItemDtoList")
    List<RecoveryOrderItemDto> mapOrderItemListToRecoveryOrderItemDtoList(List<OrderItem> orderItem);

    @Named("mapOrderItemToRecoveryOrderItemDto")
    RecoveryOrderItemDto mapOrderItemToRecoveryOrderItemDto(OrderItem orderItem);

    @Named("mapDeliveryDataToRecoveryDeliveryData")
    RecoveryDeliveryData mapDeliveryDataToRecoveryDeliveryData(DeliveryData deliveryData);
}
