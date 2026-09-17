package com.arya.ecommerce_order_management.controller;

import com.arya.ecommerce_order_management.dto.request.CreateProductRequest;
import com.arya.ecommerce_order_management.dto.request.UpdateProductRequest;
import com.arya.ecommerce_order_management.dto.response.PageResponse;
import com.arya.ecommerce_order_management.dto.response.ProductResponse;
import com.arya.ecommerce_order_management.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        // ↑ all have defaults → /products works without any params
        // /products?page=1&size=5&sortBy=price&sortDir=asc also works

        Pageable pageable = createPageable(page, size, sortBy, sortDir);

        return ResponseEntity.ok(
                productService.getAllProducts(pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Pageable pageable = createPageable(page, size, sortBy, sortDir);

        return ResponseEntity.ok(
                productService.getAllProductsByCategoryId(
                        categoryId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Search doesn't need sort (sorted by relevance)

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.searchProducts(keyword, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request){

        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    private Pageable createPageable(
            int page, int size, String sortBy, String sortDir) {

        // Validate sort direction
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        // WHY default to DESC?
        // Latest products first is better UX
        // If invalid direction given → default to DESC (no error)

        // Validate page size (prevent huge requests)
        int validSize = Math.min(size, 50);
        // WHY max 50?
        // Prevent: GET /products?size=1000000
        // Caps maximum at 50 items per page
        // Protect DB and server from abuse

        return PageRequest.of(page, validSize,
                Sort.by(direction, sortBy));
    }

}
