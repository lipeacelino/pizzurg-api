package com.pizzurg.api.repository;

import com.pizzurg.api.entity.Order;
import com.pizzurg.api.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.id = :orderId and o.user.id = :userId")
    Optional<Order> findByUserIdAndOrderId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Query("select o from Order o where o.user.id = :userId")
    Page<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    Page<Order> findByStatus(Status statusName, Pageable pageable);

    Page<Order> findAll(Pageable pageable);
}
