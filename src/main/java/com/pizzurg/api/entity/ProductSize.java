package com.pizzurg.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="product_sizes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "size_name")
    private String sizeName;
    private String description;
    @Builder.Default
    private Boolean available = true;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

}
