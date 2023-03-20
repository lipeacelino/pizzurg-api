package com.pizzurg.api.dto.input.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginUserDto(
        @Email(message = "{email.message}") String email,
        @Size(message = "{size.message}", min = 8, max = 32) String password) {
}
