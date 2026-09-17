package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.CreateProductRequest;
import com.arya.ecommerce_order_management.dto.request.UpdateProductRequest;
import com.arya.ecommerce_order_management.dto.response.PageResponse;
import com.arya.ecommerce_order_management.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProductById(Long id);
    PageResponse<ProductResponse> getAllProducts(Pageable pageable);
    PageResponse<ProductResponse> getAllProductsByCategoryId(Long categoryId, Pageable pageable);
    PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable);
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProductById(Long id);
}
