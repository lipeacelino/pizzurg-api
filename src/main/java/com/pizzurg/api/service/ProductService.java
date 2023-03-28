package com.pizzurg.api.service;

import com.pizzurg.api.dto.input.product.CreateProductDto;
import com.pizzurg.api.dto.input.product.SearchProductDto;
import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.dto.input.product.UpdateProductSizeDto;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.entity.Product;
import com.pizzurg.api.entity.ProductSize;
import com.pizzurg.api.enums.Category;
import com.pizzurg.api.exception.ProductNotFoundException;
import com.pizzurg.api.exception.ProductSizeNotFoundException;
import com.pizzurg.api.exception.ProductUnavailableException;
import com.pizzurg.api.mapper.ProductMapper;
import com.pizzurg.api.repository.ProductRepository;
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
    private ProductMapper productMapper;

    public RecoveryProductDto createProduct(CreateProductDto productDto) {
        List<ProductSize> productSizeList =  productDto.productSizes().stream()
                .map(productSizeDto -> productMapper.productSizeFromCreateProductSizeDto(productSizeDto))
                .toList();

        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .category(removeAccentsAndReturnTypeCategoryEnum(productDto.category()))
                .productSizeList(productSizeList)
                //verifica se algum valor foi passado para o available, se não ele seta com true por padrão
                .available((productDto.available()!=null) ? productDto.available() : true)
                .build();

        /*se o product estiver com o available = false, por padrão todos os productSizes devem estar com available false também,
        porque não faria sentido o produto estar estar indisponível e os tamanhos daquele produto estarem disponíveis*/
        if (product.getAvailable()) {
            product.getProductSizeList().forEach(productSize -> productSize.setAvailable(true));
        } else {
            product.getProductSizeList().forEach(productSize -> productSize.setAvailable(false));
        }

        //relaciona cada productSize com o product
        productSizeList.forEach(productSize -> productSize.setProduct(product));

        Product productSaved = productRepository.save(product);
        return productMapper.recoveryProductDtoFromProduct(productSaved);
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

    public RecoveryProductDto findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.recoveryProductDtoFromProduct(product);
    }

    public RecoveryProductDto updateProductPart(Long id, UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        //não faz sentido atualizar a categoria, porque um produto que é uma pizza não pode ser um hambúrguer e vice-versa
        if (updateProductDto.name() != null) {
            product.setName(updateProductDto.name());
        }
        if (updateProductDto.description() != null) {
            product.setDescription(updateProductDto.description());
        }
        if (updateProductDto.available() != null) {
            product.setAvailable(updateProductDto.available());

            /*se o product estiver com o available = false, todos os productSizes devem ser setados com available false também,
            porque não faria sentido o produto estar estar indisponível e os tamanhos daquele produto estarem disponíveis*/
            if (!product.getAvailable()) {
                product.getProductSizeList().forEach(productSize -> productSize.setAvailable(false));
            }
        }

        Product productSaved = productRepository.save(product);
        return productMapper.recoveryProductDtoFromProduct(productSaved);
    }

    public RecoveryProductDto updateProductSize(Long productId, Long productSizeId, UpdateProductSizeDto updateProductSizeDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        ProductSize productSize = product.getProductSizeList().stream()
                .filter(productSizeInList -> productSizeInList.getId().equals(productSizeId))
                .findFirst()
                .orElseThrow(ProductSizeNotFoundException::new);

        if (updateProductSizeDto.sizeName() != null) {
            productSize.setSizeName(updateProductSizeDto.sizeName());
        }
        if (updateProductSizeDto.description() != null) {
            productSize.setDescription(updateProductSizeDto.description());
        }
        if (updateProductSizeDto.price() != null) {
            productSize.setPrice(updateProductSizeDto.price());
        }
        if (updateProductSizeDto.available() != null) {
            if (updateProductSizeDto.available() && !product.getAvailable()) {
                throw new ProductUnavailableException();
            }
            productSize.setAvailable(updateProductSizeDto.available());
        }

        Product productSaved = productRepository.save(product);
        return productMapper.recoveryProductDtoFromProduct(productSaved);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(id);
    }
    private Category removeAccentsAndReturnTypeCategoryEnum(String category) {
        String categoryNormalized = Normalizer.normalize(category, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return Category.valueOf(categoryNormalized.toUpperCase());
    }
}