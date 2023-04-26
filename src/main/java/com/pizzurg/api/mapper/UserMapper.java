package com.pizzurg.api.mapper;

import com.pizzurg.api.dto.output.user.RecoveryRoleDto;
import com.pizzurg.api.dto.output.user.RecoveryUserDto;
import com.pizzurg.api.entity.Role;
import com.pizzurg.api.entity.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(qualifiedByName = "mapRoleListToRoleDtoList", source = "roleList", target = "roles")
    @Named("mapUserToUserDto")
    RecoveryUserDto mapUserToUserDto(User user);

    @Named("mapRoleListToRoleDtoList")
    @IterableMapping(qualifiedByName = "mapRoleToRoleDto")
    List<RecoveryRoleDto> mapRoleListToRoleDtoList(List<Role> role);

    @Named("mapRoleToRoleDto")
    RecoveryRoleDto mapRoleToRoleDto(Role role);
}
