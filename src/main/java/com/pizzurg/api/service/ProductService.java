package com.pizzurg.api.service;

import com.pizzurg.api.dto.input.product.*;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.dto.output.product.RecoveryProductVariationDto;
import com.pizzurg.api.entity.Product;
import com.pizzurg.api.entity.ProductVariation;
import com.pizzurg.api.enums.Category;
import com.pizzurg.api.exception.ProductNotFoundException;
import com.pizzurg.api.exception.ProductVariationNotFoundException;
import com.pizzurg.api.mapper.ProductMapper;
import com.pizzurg.api.repository.ProductRepository;
import com.pizzurg.api.repository.ProductVariationRepository;
import com.pizzurg.api.service.strategy.product.ProductUpdate;
import com.pizzurg.api.service.strategy.productvariation.ProductVariationUpdate;
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

    @Autowired
    private List<ProductUpdate> productUpdateList;
    @Autowired
    private List<ProductVariationUpdate> productVariationUpdateList;

    public RecoveryProductDto createProduct(CreateProductDto productDto) {
        List<ProductVariation> productVariationList =  productDto.productVariations().stream()
                .map(productSizeDto -> productMapper.productVariationFromCreateProductVariationDto(productSizeDto))
                .toList();
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .category(removeAccentsAndReturnTypeCategoryEnum(productDto.category()))
                .productVariationList(productVariationList)
                //verifica se algum valor foi passado para o available, se não ele seta com true por padrão
                .available((productDto.available()!=null) ? productDto.available() : true)
                .build();

        /*se o product estiver com o available = false, por padrão todos os productSizes devem estar com available false também,
        porque não faria sentido o produto estar estar indisponível e os tamanhos daquele produto estarem disponíveis*/
        if (product.getAvailable()) {
            product.getProductVariationList().forEach(productSize -> productSize.setAvailable(true));
        } else {
            product.getProductVariationList().forEach(productSize -> productSize.setAvailable(false));
        }

        //relaciona cada productVariation com o product
        productVariationList.forEach(productVariation -> productVariation.setProduct(product));
        Product productSaved = productRepository.save(product);
        return productMapper.recoveryProductDtoFromProduct(productSaved);
    }

    public RecoveryProductVariationDto createProductSize(Long productId, CreateProductVariationDto createProductVariationDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        ProductVariation productVariation = productMapper.productVariationFromCreateProductVariationDto(createProductVariationDto);
        productVariation.setProduct(product);
        ProductVariation productVariationSaved = productVariationRepository.save(productVariation);

        product.getProductVariationList().add(productVariationSaved);
        productRepository.save(product);

        return productMapper.recoveryProductVariationDtoFromProductVariation(productVariationSaved);
    }

    public Page<RecoveryProductDto> recoveryProducts(Pageable pageable) {
        Page<Product> productList = productRepository.findAll(pageable);
        return productList.map(product -> productMapper.recoveryProductDtoFromProduct(product));
    }

    public Page<RecoveryProductDto> recoveryProductsByCategory(String categoryName, Pageable pageable) {
        Page<Product> productList = productRepository.findByCategory(removeAccentsAndReturnTypeCategoryEnum(categoryName), pageable);
        return productList.map(product -> productMapper.recoveryProductDtoFromProduct(product));
    }
    public Page<RecoveryProductDto> recoveryProductsByNameContaining(SearchProductDto searchProductDto, Pageable pageable) {
        Page<Product> productList = productRepository.findByNameContaining(searchProductDto.name(), pageable);
        return productList.map(product -> productMapper.recoveryProductDtoFromProduct(product));
    }

    public RecoveryProductDto findProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return productMapper.recoveryProductDtoFromProduct(product);
    }

    public RecoveryProductDto updateProductPart(Long productId, UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        //invocando todas as estratégias que foram criadas para atualizar um produto
        productUpdateList.forEach(productUpdate -> productUpdate.update(product, updateProductDto));
        return productMapper.recoveryProductDtoFromProduct(productRepository.save(product));
    }

    public RecoveryProductDto updateProductVariation(Long productId, Long productVariationId, UpdateProductVariationDto updateProductVariationDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        ProductVariation productVariation = product.getProductVariationList().stream()
                .filter(productVariationInList -> productVariationInList.getId().equals(productVariationId))
                .findFirst()
                .orElseThrow(ProductVariationNotFoundException::new);

        productVariationUpdateList.forEach(productVariationUpdate -> productVariationUpdate.update(productVariation, updateProductVariationDto));

        Product productSaved = productRepository.save(product);
        return productMapper.recoveryProductDtoFromProduct(productSaved);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(productId);
    }
    public void deleteProductVariation(Long productId, Long variationId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        if (!productVariationRepository.existsById(variationId)) {
            throw new ProductVariationNotFoundException();
        }
        productVariationRepository.deleteById(variationId);
    }
    private Category removeAccentsAndReturnTypeCategoryEnum(String category) {
        String categoryNormalized = Normalizer.normalize(category, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return Category.valueOf(categoryNormalized.toUpperCase());
    }
}