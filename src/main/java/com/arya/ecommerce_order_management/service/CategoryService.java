package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.CreateCategoryRequest;
import com.arya.ecommerce_order_management.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(Long id, CreateCategoryRequest request);
    void deleteCategory(Long id);

}
