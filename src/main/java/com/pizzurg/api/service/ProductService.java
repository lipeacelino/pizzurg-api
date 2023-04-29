package com.pizzurg.api.service;

import com.pizzurg.api.dto.input.product.*;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.dto.output.product.RecoveryProductVariationDto;
import com.pizzurg.api.entity.Product;
import com.pizzurg.api.entity.ProductVariation;
import com.pizzurg.api.enums.Category;
import com.pizzurg.api.exception.ProductNotFoundException;
import com.pizzurg.api.exception.ProductVariationNotFoundException;
import com.pizzurg.api.exception.ProductVariationUnavailableException;
import com.pizzurg.api.mapper.ProductMapper;
import com.pizzurg.api.repository.ProductRepository;
import com.pizzurg.api.repository.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private ProductMapper productMapper;

    public RecoveryProductDto createProduct(CreateProductDto productDto) {
        List<ProductVariation> productVariationList =  productDto.productVariations().stream()
                .map(productVariationDto -> productMapper.mapCreateProductVariationDtoToProductVariation(productVariationDto))
                .toList();
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .category(removeAccentsAndReturnTypeCategoryEnum(productDto.category()))
                .productVariationList(productVariationList)
                .available(productDto.available())
                .build();

        /*se o product estiver com o available = false, por padrão todos os productVariation devem estar com available false também,
        porque não faria sentido o produto estar estar indisponível e os tamanhos daquele produto estarem disponíveis*/
        if (!product.getAvailable() && product.getProductVariationList().stream().anyMatch(ProductVariation::getAvailable)) {
                throw new ProductVariationUnavailableException();
        }

        //relaciona cada productVariation com o product
        productVariationList.forEach(productVariation -> productVariation.setProduct(product));
        Product productSaved = productRepository.save(product);
        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    public RecoveryProductDto createProductVariation(Long productId, CreateProductVariationDto createProductVariationDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        ProductVariation productVariation = productMapper.mapCreateProductVariationDtoToProductVariation(createProductVariationDto);
        productVariation.setProduct(product);
        ProductVariation productVariationSaved = productVariationRepository.save(productVariation);

        product.getProductVariationList().add(productVariationSaved);
        productRepository.save(product);

        return productMapper.mapProductToRecoveryProductDto(productVariationSaved.getProduct());
    }

    public Page<RecoveryProductDto> recoveryProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public Page<RecoveryProductDto> recoveryProductsByCategory(String categoryName, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategory(removeAccentsAndReturnTypeCategoryEnum(categoryName), pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public Page<RecoveryProductDto> recoveryProductsByName(String productName, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContaining(productName, pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public RecoveryProductDto findProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return productMapper.mapProductToRecoveryProductDto(product);
    }

    public RecoveryProductDto updateProductPart(Long productId, UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (updateProductDto.name() != null) {
            product.setName(updateProductDto.name());
        }
        if (updateProductDto.description() != null) {
            product.setDescription(updateProductDto.description());
        }
        if (updateProductDto.available() != null) {
            product.setAvailable(updateProductDto.available());

            /*se o product estiver com o available = false, todos os productVariation devem ser setados com available false também,
            porque não faria sentido o produto estar estar indisponível e os tamanhos daquele produto estarem disponíveis*/
            if (!product.getAvailable()) {
                product.getProductVariationList().forEach(productVariation -> productVariation.setAvailable(false));
            }
        }

        return productMapper.mapProductToRecoveryProductDto(productRepository.save(product));
    }

    public RecoveryProductDto updateProductVariation(Long productId, Long productVariationId, UpdateProductVariationDto updateProductVariationDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        ProductVariation productVariation = product.getProductVariationList().stream()
                .filter(productVariationInList -> productVariationInList.getId().equals(productVariationId))
                .findFirst()
                .orElseThrow(ProductVariationNotFoundException::new);

        if (updateProductVariationDto.sizeName() != null) {
            productVariation.setSizeName(updateProductVariationDto.sizeName());
        }
        if (updateProductVariationDto.description() != null) {
            productVariation.setDescription(updateProductVariationDto.description());
        }
        if (updateProductVariationDto.available() != null) {
            if (updateProductVariationDto.available() && !productVariation.getProduct().getAvailable()) {
                throw new ProductVariationUnavailableException();
            }
            productVariation.setAvailable(updateProductVariationDto.available());
        }
        if (updateProductVariationDto.price() != null) {
            productVariation.setPrice(updateProductVariationDto.price());
        }

        Product productSaved = productRepository.save(product);
        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(productId);
    }

    public void deleteProductVariation(Long productId, Long productVariationId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        if (!productVariationRepository.existsById(productVariationId)) {
            throw new ProductVariationNotFoundException();
        }
        productVariationRepository.deleteById(productVariationId);
    }

    private Category removeAccentsAndReturnTypeCategoryEnum(String category) {
        String categoryNormalized = Normalizer.normalize(category, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return Category.valueOf(categoryNormalized.toUpperCase());
    }

}