package com.pizzurg.api.services;

import com.pizzurg.api.dto.input.product.*;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.entities.Product;
import com.pizzurg.api.entities.ProductVariation;
import com.pizzurg.api.enums.Category;
import com.pizzurg.api.exception.*;
import com.pizzurg.api.mappers.ProductMapper;
import com.pizzurg.api.repositories.OrderItemRepository;
import com.pizzurg.api.repositories.ProductRepository;
import com.pizzurg.api.repositories.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductMapper productMapper;

    public RecoveryProductDto createProduct(CreateProductDto createProductDto) {
        /*
        Converte a lista de ProductVariationDto em uma lista de ProductVariation,
        utilizando o ProductMapper para fazer o mapeamento de cada elemento da lista.
         */
        List<ProductVariation> productVariations =  createProductDto.productVariations().stream()
                .map(productVariationDto -> productMapper.mapCreateProductVariationDtoToProductVariation(productVariationDto))
                .toList();

        // Cria um produto através dos dados do DTO
        Product product = Product.builder()
                .name(createProductDto.name())
                .description(createProductDto.description())
                .category(Category.valueOf(createProductDto.category().toUpperCase()))
                .productVariations(productVariations)
                .available(createProductDto.available())
                .build();

        /*
        Se o produto estiver com o available = false, por padrão todas as variações do produto devem estar com available false também,
        porque não faria sentido o produto estar estar indisponível e as variações daquele produto estarem disponíveis
         */
        if (!product.getAvailable() && product.getProductVariations().stream().anyMatch(ProductVariation::getAvailable)) {
                throw new ProductVariationUnavailableException();
        }

        // Relaciona cada variação de produto com o produto
        productVariations.forEach(productVariation -> productVariation.setProduct(product));
        Product productSaved = productRepository.save(product);
        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    public RecoveryProductDto createProductVariation(Long productId, CreateProductVariationDto createProductVariationDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        // Converte o DTO de criação da variação de produto para uma entidade ProductVariation
        ProductVariation productVariation = productMapper.mapCreateProductVariationDtoToProductVariation(createProductVariationDto);

        // Associa a variação de produto ao produto e salva a variação no banco de dados
        productVariation.setProduct(product);
        ProductVariation productVariationSaved = productVariationRepository.save(productVariation);

        // Adiciona a variação de produto ao produto e salva o produto no banco de dados
        product.getProductVariations().add(productVariationSaved);
        productRepository.save(product);

        return productMapper.mapProductToRecoveryProductDto(productVariationSaved.getProduct());
    }

    public Page<RecoveryProductDto> getProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public Page<RecoveryProductDto> getProductsByCategory(String categoryName, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategory(Category.valueOf(categoryName.toUpperCase()), pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public Page<RecoveryProductDto> getProductsByName(String productName, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContaining(productName, pageable);
        return productPage.map(product -> productMapper.mapProductToRecoveryProductDto(product));
    }

    public RecoveryProductDto getProductById(Long productId) {
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

            /*
            Se o produto estiver com o available = false, por padrão todas as variações do produto devem estar com available = false também,
            porque não faria sentido o produto estar estar indisponível e as variações daquele produto estarem disponíveis
             */
            if (!product.getAvailable()) {
                product.getProductVariations().forEach(productVariation -> productVariation.setAvailable(false));
            }
        }

        return productMapper.mapProductToRecoveryProductDto(productRepository.save(product));
    }

    public RecoveryProductDto updateProductVariation(Long productId, Long productVariationId, UpdateProductVariationDto updateProductVariationDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        /*
         Procura pela variação de produto (através do id) na lista de variações do produto
         que já está salvo no banco
         */
        ProductVariation productVariation = product.getProductVariations().stream()
                .filter(productVariationInProduct -> productVariationInProduct.getId().equals(productVariationId))
                .findFirst()
                .orElseThrow(ProductVariationNotFoundException::new);

        if (updateProductVariationDto.sizeName() != null) {
            productVariation.setSizeName(updateProductVariationDto.sizeName());
        }
        if (updateProductVariationDto.description() != null) {
            productVariation.setDescription(updateProductVariationDto.description());
        }
        if (updateProductVariationDto.available() != null) {
            /*
            Se o produto estiver com o available = false, por padrão a nova variação adicionada deve estar com available = false também,
            porque não faria sentido o produto estar estar indisponível e a variação daquele produto estar disponível
             */
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

    public void deleteProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        // Se o produto estiver associado a um pedido, o produto não pode ser excluído
        if (orderItemRepository.findFirstByProductId(productId).isPresent()) {
            throw new ProductAssociatedWithOrderException();
        }
        productRepository.deleteById(productId);
    }

    public void deleteProductVariationById(Long productId, Long productVariationId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        if (!productVariationRepository.existsById(productVariationId)) {
            throw new ProductVariationNotFoundException();
        }
        // Se uma variação do produto estiver associada a um pedido, a variação não pode ser excluída
        if (orderItemRepository.findFirstByProductVariationId(productVariationId).isPresent()) {
            throw new ProductVariationAssociatedWithOrderException();
        }
        productVariationRepository.deleteById(productVariationId);
    }

}