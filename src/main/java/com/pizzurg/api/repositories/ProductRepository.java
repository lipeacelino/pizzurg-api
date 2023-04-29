package com.pizzurg.api.repositories;

import com.pizzurg.api.entities.Product;
import com.pizzurg.api.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(Category categoryName, Pageable pageable);

    Page<Product> findByNameContaining(String name, Pageable pageable);

}
