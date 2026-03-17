package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
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
    ResponseEntity<SuccessApi<ProductDetailsResponse>> createProduct(@Valid @RequestBody ProductCreationRequest r) {
        ProductDetailsResponse response = productService.createProduct(r);
        return ApiFactory.success(response, "Create product successfully.");
    }

    @GetMapping("/{slug}")
    ResponseEntity<SuccessApi<ProductDetailsResponse>> getProductBySlug(@PathVariable String slug) {
        ProductDetailsResponse response = productService.getProductBySlug(slug);
        return ApiFactory.success(response, "Get product successfully.");
    }

    @GetMapping
    ResponseEntity<SuccessApi<PageResponse<ProductResponse>>> getSummaryProducts(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortBy", defaultValue = "newest", required = false) String sortBy

    ) {
        PageResponse<ProductResponse> responses = productService.getProducts(pageNumber, pageSize, keyword, sortBy);
        return ApiFactory.success(responses, "Get products successfully.");
    }
}
