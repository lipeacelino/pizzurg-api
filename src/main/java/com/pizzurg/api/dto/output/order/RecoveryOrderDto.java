package com.pizzurg.api.dto.output.order;

import com.pizzurg.api.dto.output.user.RecoveryUserDto;
import com.pizzurg.api.enums.MethodPayment;
import com.pizzurg.api.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RecoveryOrderDto(
        Long id,
        RecoveryUserDto user,
        List<RecoveryOrderItemDto> orderItems,
        Status status,
        MethodPayment methodPayment,
        BigDecimal amount,
        RecoveryDeliveryData deliveryData,
        LocalDateTime createdDate
) {
}
