package com.arya.ecommerce_order_management.controller;

import com.arya.ecommerce_order_management.dto.request.CreateCategoryRequest;
import com.arya.ecommerce_order_management.dto.response.CategoryResponse;
import com.arya.ecommerce_order_management.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "2. Categories",
        description = "Category management. " +
                "GET endpoints are public. " +
                "POST/PUT/DELETE require ADMIN role."
)
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create category", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Category created"),
            @ApiResponse(responseCode = "403",
                    description = "Access denied - ADMIN role required"),
            @ApiResponse(responseCode = "409",
                    description = "Category name already exists")
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CreateCategoryRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request));
    }

    @Operation(summary = "Get category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Category found"),
            @ApiResponse(responseCode = "404",
                    description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Get all categories", description = "Public endpoint")
    @ApiResponse(responseCode = "200", description = "List of all categories")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Update category", description = "ADMIN only")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CreateCategoryRequest request) {

        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Delete category", description = "ADMIN only")
    @ApiResponse(responseCode = "204", description = "Category deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }

}
