package com.pizzurg.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="product_variations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductVariation { //mudar para o nome ProductVariation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sizeName;

    private String description;

    @Builder.Default
    private Boolean available = true;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}