package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.shared.dto.response.ApiResponse;
import com.sam.miniecommerceapi.shared.dto.response.PageResponse;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.CategoryUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Category endpoints")
@RequestMapping("/api/v1/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping()
    @Operation(summary = "Create category")
    ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryCreationRequest r) {
        CategoryResponse response = categoryService.createCategory(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update category by ID")
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest r
    ) {
        CategoryResponse response = categoryService.updateCategory(id, r);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @Operation(summary = "Get all categories")
    @GetMapping()
    ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        PageResponse<CategoryResponse> responses = categoryService.getCategories(pageNumber, pageSize);
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get category by ID")
    ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @Operation(summary = "Get category by slug")
    @GetMapping("/slug/{slug}")
    ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse response = categoryService.getCategorySlug(slug);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @Operation(summary = "Get category by name")
    @GetMapping("/name/{name}")
    ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByName(@PathVariable String name) {
        CategoryResponse response = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
