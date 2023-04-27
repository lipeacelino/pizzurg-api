package com.pizzurg.api.controller;

import com.pizzurg.api.dto.input.product.*;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.dto.output.product.RecoveryProductVariationDto;
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

    @PostMapping({"/{productId}"})
    public ResponseEntity<RecoveryProductVariationDto> createProductVariation(@PathVariable Long productId,
                                                                              @RequestBody @Valid CreateProductVariationDto createProductVariationDto) {
        return new ResponseEntity<>(productService.createProductVariation(productId, createProductVariationDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<RecoveryProductDto>> recoveryProducts(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable) {
        return new ResponseEntity<>(productService.recoveryProducts(pageable), HttpStatus.OK);
    }
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<RecoveryProductDto>> recoveryProductsByCategory(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @PathVariable String categoryName
    ) {
        return new ResponseEntity<>(productService.recoveryProductsByCategory(categoryName, pageable), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RecoveryProductDto>> recoveryProductsByNameContaining(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @RequestBody SearchProductDto searchproductDto) {
        return new ResponseEntity<>(productService.recoveryProductsByNameContaining(searchproductDto, pageable), HttpStatus.OK);
    }

    @GetMapping("/{productId}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> findProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.findProductById(productId), HttpStatus.OK);
    }

    @PatchMapping("/{productId}") //ver se dá pra validar a path variable
    public ResponseEntity<RecoveryProductDto> updateProductPart(@PathVariable Long productId, @RequestBody @Valid UpdateProductDto updateProductDto) {
        return new ResponseEntity<>(productService.updateProductPart(productId, updateProductDto), HttpStatus.OK);
    }

    @PutMapping("/{productId}/sizes/{productVariationId}")
    public ResponseEntity<RecoveryProductDto> updateProductVariation(@PathVariable Long productId, @PathVariable Long productVariationId,
                                                                @RequestBody @Valid UpdateProductVariationDto updateProductVariationDto) {
        return new ResponseEntity<>(productService.updateProductVariation(productId, productVariationId, updateProductVariationDto), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}") //ver se dá pra validar a path variable
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{productId}/variation/{variationId}") //ver se dá pra validar a path variable
    public ResponseEntity<Void> deleteProductVariation(@PathVariable Long productId, @PathVariable Long variationId) {
        productService.deleteProductVariation(productId, variationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
