package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.product.dto.request.CategoryCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.CategoryResponse;
import com.sam.miniecommerceapi.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping()
    ResponseEntity<SuccessApi<CategoryResponse>> createCategory(@Valid @RequestBody CategoryCreationRequest r) {
        CategoryResponse response = categoryService.createCategory(r);
        return ApiFactory.success(response, "Create category successfully.");
    }
}
