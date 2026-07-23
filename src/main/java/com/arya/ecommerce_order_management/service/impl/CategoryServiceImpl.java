package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.CreateCategoryRequest;
import com.arya.ecommerce_order_management.dto.response.CategoryResponse;
import com.arya.ecommerce_order_management.entity.Category;
import com.arya.ecommerce_order_management.exception.DuplicateResourceException;
import com.arya.ecommerce_order_management.exception.ResourceNotFoundException;
import com.arya.ecommerce_order_management.repository.CategoryRepository;
import com.arya.ecommerce_order_management.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())){
            throw new DuplicateResourceException("Category",  "name", request.name());
        }

        Category category = Category.builder()
                .name(request.name())
                .build();

        Category saved = categoryRepository.save(category);

        return CategoryResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        return CategoryResponse.from(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (categoryRepository.existsByName(request.name())){
            throw new DuplicateResourceException("Category",  "name", request.name());
        }

        category.setName(request.name());

        Category updated = categoryRepository.save(category);

        return CategoryResponse.from(updated);

    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Category", "id", id);
        }

        categoryRepository.deleteById(id);
    }

}
