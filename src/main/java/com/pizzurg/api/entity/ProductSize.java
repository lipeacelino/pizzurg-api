package com.pizzurg.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

}
