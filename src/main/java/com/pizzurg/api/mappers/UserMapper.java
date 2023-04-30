package com.pizzurg.api.mappers;

import com.pizzurg.api.dto.output.user.RecoveryRoleDto;
import com.pizzurg.api.dto.output.user.RecoveryUserDto;
import com.pizzurg.api.entities.Role;
import com.pizzurg.api.entities.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(qualifiedByName = "mapRoleListToRoleDtoList", target = "roles")
    @Named("mapUserToUserDto")
    RecoveryUserDto mapUserToUserDto(User user);

    @Named("mapRoleListToRoleDtoList")
    @IterableMapping(qualifiedByName = "mapRoleToRoleDto")
    List<RecoveryRoleDto> mapRoleListToRoleDtoList(List<Role> roles);

    @Named("mapRoleToRoleDto")
    RecoveryRoleDto mapRoleToRoleDto(Role role);

}
