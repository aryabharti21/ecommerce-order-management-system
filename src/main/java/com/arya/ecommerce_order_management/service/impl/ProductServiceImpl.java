package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.CreateProductRequest;
import com.arya.ecommerce_order_management.dto.request.UpdateProductRequest;
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

        inventoryRepository.save(inventory);

        return ProductResponse.from(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return ProductResponse.from(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProductsByCategoryId(Long id) {

        if (categoryRepository.existsById(id)) throw new ResourceNotFoundException("Category", "id", id);

        return productRepository.findByCategoryId(id)
                .stream()
                .map(ProductResponse::from)
                .toList();
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

        return ProductResponse.from(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String keyword) {

        if (keyword == null || keyword.isBlank()) return List.of();

        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(ProductResponse::from)
                .toList();
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
