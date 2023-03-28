package com.pizzurg.api.controller;

import com.pizzurg.api.dto.input.product.CreateProductDto;
import com.pizzurg.api.dto.input.product.SearchProductDto;
import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.dto.input.product.UpdateProductSizeDto;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<RecoveryProductDto>> recoveryProducts(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable) {
        return new ResponseEntity<>(productService.recoveryProducts(pageable), HttpStatus.OK);
    }
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<RecoveryProductDto>> recoveryProductsByCategory(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @PathVariable String categoryName) {
        return new ResponseEntity<>(productService.recoveryProductsByCategory(categoryName, pageable), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RecoveryProductDto>> recoveryProductsByNameContaining(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @RequestBody SearchProductDto searchproductDto) {
        return new ResponseEntity<>(productService.recoveryProductsByNameContaining(searchproductDto, pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> findProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> updateProductPart(@PathVariable Long id, @RequestBody @Valid UpdateProductDto updateProductDto) {
        return new ResponseEntity<>(productService.updateProductPart(id, updateProductDto), HttpStatus.OK);
    }

    @PutMapping("/{productId}/sizes/{productSizeId}")
    public ResponseEntity<RecoveryProductDto> updateProductSize(@PathVariable Long productId, @PathVariable Long productSizeId,
                                                                @RequestBody @Valid UpdateProductSizeDto updateProductSizeDto) {
        return new ResponseEntity<>(productService.updateProductSize(productId, productSizeId , updateProductSizeDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}") //ver se dá pra validar a path variable
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
