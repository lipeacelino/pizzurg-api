package com.pizzurg.api.service;

import com.pizzurg.api.entity.Role;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role getRole(RoleName roleName) {
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        Role role = Role.builder().name(roleName).build();
        return roleRepository.save(role);
    }
}
