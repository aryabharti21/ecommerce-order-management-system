package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.CreateProductRequest;
import com.arya.ecommerce_order_management.dto.request.UpdateProductRequest;
import com.arya.ecommerce_order_management.dto.response.PageResponse;
import com.arya.ecommerce_order_management.dto.response.ProductResponse;
import com.arya.ecommerce_order_management.entity.Category;
import com.arya.ecommerce_order_management.entity.Inventory;
import com.arya.ecommerce_order_management.entity.Product;
import com.arya.ecommerce_order_management.entity.enums.InventoryStatus;
import com.arya.ecommerce_order_management.exception.DuplicateResourceException;
import com.arya.ecommerce_order_management.exception.ResourceNotFoundException;
import com.arya.ecommerce_order_management.repository.CategoryRepository;
import com.arya.ecommerce_order_management.repository.InventoryRepository;
import com.arya.ecommerce_order_management.repository.ProductRepository;
import com.arya.ecommerce_order_management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsByNameAndCategoryId(request.name(), request.categoryId())) {
            throw new DuplicateResourceException("Product", "name", request.name());
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));

        Product product = Product.builder()
                .name(request.name())
                .category(category)
                .price(request.price())
                .description(request.description())
                .build();

        Product savedProduct = productRepository.save(product);

        Inventory inventory = Inventory.builder()
                .product(savedProduct)
                .itemCount(request.initialStock())
                .status(request.initialStock() > 0 ? InventoryStatus.AVAILABLE : InventoryStatus.OUT_OF_STOCK)
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        return ProductResponse.from(savedProduct,savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        Inventory inventory = inventoryRepository.findByProductId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));

        return ProductResponse.from(product,inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        Page<ProductResponse> responsePage = productPage
                .map(product -> {
                    Inventory inventory = inventoryRepository
                            .findByProductId(product.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Inventory", "productId", product.getId()));
                    return ProductResponse.from(product, inventory);
                });

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProductsByCategoryId(
            Long categoryId, Pageable pageable) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }

        Page<Product> productPage = productRepository
                .findByCategoryId(categoryId, pageable);
        // @EntityGraph handles JOIN FETCH on category

        Page<ProductResponse> responsePage = productPage
                .map(product -> {
                    Inventory inventory = inventoryRepository
                            .findByProductId(product.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Inventory", "productId", product.getId()));
                    return ProductResponse.from(product, inventory);
                });

        return PageResponse.from(responsePage);
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchProducts(
            String keyword, Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {
            return PageResponse.from(Page.empty(pageable));
            // Return empty page instead of empty list
            // Page.empty(pageable) creates a Page with 0 elements
            // Keeps consistent PageResponse format
        }

        Page<Product> productPage = productRepository
                .findByNameContainingIgnoreCase(keyword, pageable);

        Page<ProductResponse> responsePage = productPage
                .map(product -> {
                    Inventory inventory = inventoryRepository
                            .findByProductId(product.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Inventory", "productId", product.getId()));
                    return ProductResponse.from(product, inventory);
                });

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.categoryId()));

        if (!existingProduct.getName().equals(request.name())) {
            if (productRepository.existsByNameAndCategoryId(request.name(), request.categoryId()))
                throw new DuplicateResourceException("Product", "name", request.name());
        }

        existingProduct.setName(request.name());
        existingProduct.setCategory(category);
        existingProduct.setPrice(request.price());
        existingProduct.setDescription(request.description());

        Product updatedProduct = productRepository.save(existingProduct);

        Inventory inventory = inventoryRepository.findByProductId(updatedProduct.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", id));

        return ProductResponse.from(updatedProduct, inventory);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }

        productRepository.deleteById(id);
    }
}
