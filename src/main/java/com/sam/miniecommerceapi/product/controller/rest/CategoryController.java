package com.sam.miniecommerceapi.product.controller.rest;

import com.sam.miniecommerceapi.shared.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.shared.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(name = "Category endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping()
    @Operation(summary = "Create category")
    ResponseEntity<SuccessApi<CategoryResponse>> createCategory(@Valid @RequestBody CategoryCreationRequest r) {
        CategoryResponse response = categoryService.createCategory(r);
        return ApiFactory.success(response, "Create category successfully.");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update category by ID")
    @PutMapping("/{id}")
    ResponseEntity<SuccessApi<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest r
    ) {
        CategoryResponse response = categoryService.updateCategory(id, r);
        return ApiFactory.success(response, "Update category successfully.");
    }

    @Operation(summary = "Get all categories")
    @GetMapping()
    ResponseEntity<SuccessApi<PageResponse<CategoryResponse>>> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        PageResponse<CategoryResponse> responses = categoryService.getCategories(pageNumber, pageSize);
        return ApiFactory.success(responses, "Get categories successfully.");
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get category by ID")
    ResponseEntity<SuccessApi<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ApiFactory.success(response, "Get category successfully.");
    }

    @Operation(summary = "Get category by slug")
    @GetMapping("/slug/{slug}")
    ResponseEntity<SuccessApi<CategoryResponse>> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse response = categoryService.getCategorySlug(slug);
        return ApiFactory.success(response, "Get category successfully.");
    }

    @Operation(summary = "Get category by name")
    @GetMapping("/name/{name}")
    ResponseEntity<SuccessApi<CategoryResponse>> getCategoryByName(@PathVariable String name) {
        CategoryResponse response = categoryService.getCategoryByName(name);
        return ApiFactory.success(response, "Get category successfully.");
    }
}
