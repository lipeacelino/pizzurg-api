package com.pizzurg.api.mappers;

import com.pizzurg.api.dto.output.order.RecoveryDeliveryData;
import com.pizzurg.api.dto.output.order.RecoveryOrderDto;
import com.pizzurg.api.dto.output.order.RecoveryOrderItemDto;
import com.pizzurg.api.entities.DeliveryData;
import com.pizzurg.api.entities.Order;
import com.pizzurg.api.entities.OrderItem;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface OrderMapper {


    @Mapping(qualifiedByName = "mapDeliveryDataToRecoveryDeliveryData", target = "deliveryData")
    @Mapping(qualifiedByName = "mapOrderItemListToRecoveryOrderItemDtoList", target = "orderItems")
    @Mapping(qualifiedByName = "mapUserToUserDto", target = "user")
    @Named("mapOrderToRecoveryOrderDto")
    RecoveryOrderDto mapOrderToRecoveryOrderDto(Order order);

    @IterableMapping(qualifiedByName = "mapOrderItemToRecoveryOrderItemDto")
    @Named("mapOrderItemListToRecoveryOrderItemDtoList")
    List<RecoveryOrderItemDto> mapOrderItemListToRecoveryOrderItemDtoList(List<OrderItem> orderItems);

    @Named("mapOrderItemToRecoveryOrderItemDto")
    RecoveryOrderItemDto mapOrderItemToRecoveryOrderItemDto(OrderItem orderItem);

    @Named("mapDeliveryDataToRecoveryDeliveryData")
    RecoveryDeliveryData mapDeliveryDataToRecoveryDeliveryData(DeliveryData deliveryData);

}
