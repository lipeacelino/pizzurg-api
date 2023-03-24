package com.pizzurg.api.controller;

import com.pizzurg.api.dto.input.product.CreateProductDto;
import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<RecoveryProductDto> createProduct(@RequestBody @Valid CreateProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecoveryProductDto>> recoveryProducts() {
        return new ResponseEntity<>(productService.recoveryProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> findProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductDto updateProductDto) {
        return new ResponseEntity<>(productService.updateProduct(id, updateProductDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}") //ver se dá pra validar a path variable
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
