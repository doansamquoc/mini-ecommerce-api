package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @PostMapping
    ResponseEntity<SuccessApi<ProductResponse>> createProduct(@Valid @RequestBody ProductCreationRequest r) {
        ProductResponse response = productService.createProduct(r);
        return ApiFactory.success(response, "Create product successfully.");
    }

    @GetMapping("/{slug}")
    ResponseEntity<SuccessApi<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        ProductResponse response = productService.getProductBySlug(slug);
        return ApiFactory.success(response, "Get product successfully.");
    }
}
