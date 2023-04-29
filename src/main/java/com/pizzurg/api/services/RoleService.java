package com.pizzurg.api.services;

import com.pizzurg.api.entities.Role;
import com.pizzurg.api.enums.RoleName;
import com.pizzurg.api.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role getRole(RoleName roleName) {
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        Role role = Role.builder().name(roleName).build();
        return roleRepository.save(role);
    }

}
