package com.pizzurg.api.repository;

import com.pizzurg.api.entity.Role;
import com.pizzurg.api.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName role);

}
