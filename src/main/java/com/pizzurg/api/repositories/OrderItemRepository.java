package com.pizzurg.api.repositories;

import com.pizzurg.api.entities.Order;
import com.pizzurg.api.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(nativeQuery = true, value = "select oi.* from order_items oi where oi.product_variation_id = :productVariationId limit 1")
    Optional<OrderItem> findFirstByProductVariationId(@Param("productVariationId") Long productVariationId);

    @Query(nativeQuery = true, value = "select oi.* from order_items oi " +
            "join product_variations pv on oi.product_variation_id = pv.id " +
            "join products p on p.id = pv.product_id where p.id = :productId limit 1\n")
    Optional<OrderItem> findFirstByProductId(@Param("productId") Long productId);

}
