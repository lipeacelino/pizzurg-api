package com.pizzurg.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pizzurg.api.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductVariation> productVariationList;
    private Boolean available;

}
