package com.pizzurg.api.dto.input.order;

import jakarta.validation.constraints.NotBlank;

public record CreateDeliveryDataDto(

        @NotBlank(message = "{not.blank.message}")
        String receiverName,

        @NotBlank(message = "{not.blank.message}")
        String address,

        @NotBlank(message = "{not.blank.message}")
        String number,

        @NotBlank(message = "{not.blank.message}")
        String complement,

        @NotBlank(message = "{not.blank.message}")
        String district,

        @NotBlank(message = "{not.blank.message}")
        String zipCode,

        @NotBlank(message = "{not.blank.message}")
        String city,

        @NotBlank(message = "{not.blank.message}")
        String state,

        @NotBlank(message = "{not.blank.message}")
        String phone_number

) {
}
