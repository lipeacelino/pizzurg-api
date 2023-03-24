package com.pizzurg.api.service;

import com.pizzurg.api.dto.input.product.CreateProductDto;
import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.entity.Product;
import com.pizzurg.api.enums.Category;
import com.pizzurg.api.exception.ProductNotFoundException;
import com.pizzurg.api.mapper.ProductMapper;
import com.pizzurg.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public RecoveryProductDto createProduct(CreateProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .price(productDto.price())
                .category(Category.valueOf(productDto.category().toUpperCase()))
                .productSizeList(productDto.productSizes())
                .build();
        //relaciona cada ProductSize com o Product
        productDto.productSizes().forEach(productSize -> {productSize.setProduct(product);});
        Product productSaved = productRepository.save(product);
        return productMapper.recoveryProdDtoToProd(productSaved);
    }

    public List<RecoveryProductDto> recoveryProducts() {
        List<Product> productList= productRepository.findAll();
        return productList.stream()
                .map(product -> productMapper.recoveryProdDtoToProd(product))
                .collect(Collectors.toList());
    }

    public RecoveryProductDto findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.recoveryProdDtoToProd(product);
    }

    public RecoveryProductDto updateProduct(Long id, UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if (updateProductDto.name() != null) {
            product.setName(product.getName());
        }
        if (updateProductDto.description() != null) {
            product.setDescription(product.getDescription());
        }
        if (updateProductDto.price() != null) {
            product.setPrice(product.getPrice());
        }
        if (updateProductDto.category() != null) {
            product.setCategory(Category.valueOf(updateProductDto.category().toUpperCase()));
        }
        if (updateProductDto.available() != null) {
            product.setAvailable(updateProductDto.available());
            if (updateProductDto.available() == false) {
                product.getProductSizeList().forEach(productSize -> productSize.setAvailable(false));
            }
        }
        Product productSaved = productRepository.save(product);
        return productMapper.recoveryProdDtoToProd(productSaved);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(id);
    }
}