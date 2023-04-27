package com.pizzurg.api.dto.input.order;

import jakarta.validation.constraints.NotBlank;

public record ChangeStatusOrderDto(

        @NotBlank(message = "{not.blank.message}")
        String status
) {
}
