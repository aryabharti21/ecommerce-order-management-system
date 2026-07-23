package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.CreateProductRequest;
import com.arya.ecommerce_order_management.dto.request.UpdateProductRequest;
import com.arya.ecommerce_order_management.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getAllProductsByCategoryId(Long categoryId);
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    List<ProductResponse> searchProducts(String keyword);
    void deleteProductById(Long id);

}
