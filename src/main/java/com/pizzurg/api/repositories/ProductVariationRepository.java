package com.pizzurg.api.repositories;

import com.pizzurg.api.entities.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {

    @Query("select pv from ProductVariation pv where pv.id = :productVariationId and pv.product.id = :productId")
    Optional<ProductVariation> findByProductIdAndProductVariationId(@Param("productId") Long productId, @Param("productVariationId") Long productVariationId);

}
