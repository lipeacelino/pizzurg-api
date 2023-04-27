package com.pizzurg.api.dto.output.user;

import java.util.List;

public record RecoveryUserDto(

        Long id,

        String email,

        List<RecoveryRoleDto> roles
) {
}
