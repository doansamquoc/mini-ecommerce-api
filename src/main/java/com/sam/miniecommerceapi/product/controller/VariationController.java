package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.service.VariantService;
import com.sam.miniecommerceapi.shared.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product Variation Endpoints.")
@RequestMapping("/api/v1/products/{productId}/variations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariationController {
    VariantService service;

    @PostMapping
    @Operation(summary = "Create a new product variation.")
    ResponseEntity<ApiResponse<VariantResponse>> createVariation(
            @PathVariable Long productId,
            @Valid @RequestBody VariantRequest request
    ) {
        VariantResponse response = service.create(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }
}
