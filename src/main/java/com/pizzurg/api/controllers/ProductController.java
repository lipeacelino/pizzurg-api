package com.pizzurg.api.controllers;

import com.pizzurg.api.dto.input.product.*;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.services.ProductService;
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
    public ResponseEntity<RecoveryProductDto> createProduct(@Valid @RequestBody CreateProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @PostMapping({"/{productId}/variation"})
    public ResponseEntity<RecoveryProductDto> createProductVariation(@PathVariable Long productId,
                                                                     @Valid @RequestBody CreateProductVariationDto createProductVariationDto) {
        return new ResponseEntity<>(productService.createProductVariation(productId, createProductVariationDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<RecoveryProductDto>> getProducts(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable) {
        return new ResponseEntity<>(productService.getProducts(pageable), HttpStatus.OK);
    }
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<RecoveryProductDto>> getProductsByCategory(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @PathVariable String categoryName
    ) {
        return new ResponseEntity<>(productService.getProductsByCategory(categoryName, pageable), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RecoveryProductDto>> getProductsByName(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @RequestParam("name") String productName) {
        return new ResponseEntity<>(productService.getProductsByName(productName, pageable), HttpStatus.OK);
    }

    @GetMapping("/{productId}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @PatchMapping("/{productId}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> updateProductPart(@PathVariable Long productId, @RequestBody @Valid UpdateProductDto updateProductDto) {
        return new ResponseEntity<>(productService.updateProductPart(productId, updateProductDto), HttpStatus.OK);
    }

    @PutMapping("/{productId}/variation/{productVariationId}")
    public ResponseEntity<RecoveryProductDto> updateProductVariation(@PathVariable Long productId, @PathVariable Long productVariationId,
                                                                @RequestBody @Valid UpdateProductVariationDto updateProductVariationDto) {
        return new ResponseEntity<>(productService.updateProductVariation(productId, productVariationId, updateProductVariationDto), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}") //ver se dá pra validar a path variable
    public ResponseEntity<Void> deleteProductById(@PathVariable Long productId) {
        productService.deleteProductId(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{productId}/variation/{productVariationId}") //ver se dá pra validar a path variable
    public ResponseEntity<Void> deleteProductVariationById(@PathVariable Long productId, @PathVariable Long productVariationId) {
        productService.deleteProductVariationById(productId, productVariationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
