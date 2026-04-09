package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.product.dto.request.VariantCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.VariantRequest;
import com.sam.miniecommerceapi.product.dto.response.VariantResponse;
import com.sam.miniecommerceapi.product.service.VariantService;
import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product Variation Endpoints")
@RequestMapping("/api/v1/products/{productId}/variations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariationController {
    VariantService service;

    @PostMapping
    @Operation(summary = "Create a new product variation.")
    ResponseEntity<ApiResponse<VariantResponse>> createVariation(
            @PathVariable Long productId,
            @Valid @RequestBody VariantCreationRequest request
    ) {
        VariantResponse response = service.create(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a product variation.")
    ResponseEntity<ApiResponse<VariantResponse>> updateVariation(
            @PathVariable Long productId,
            @PathVariable Long id,
            @Valid @RequestBody VariantRequest request
    ) {
        VariantResponse response = service.update(productId, id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @DeleteMapping
    @Operation(
            summary = "Delete all product variation.",
            description = "Soft delete. The product will not actually be deleted."
    )
    ResponseEntity<ApiResponse<VariantResponse>> deleteAll(@PathVariable Long productId) {
        service.deleteAll(productId);
        return ResponseEntity.ok(ApiResponse.of());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a product variation.",
            description = "Soft delete. The product will not actually be deleted."
    )
    ResponseEntity<ApiResponse<VariantResponse>> deleteVariation(@PathVariable Long id, @PathVariable Long productId) {
        service.delete(productId, id);
        return ResponseEntity.ok(ApiResponse.of());
    }

    @GetMapping
    @Operation(summary = "Get all product variations.")
    ResponseEntity<ApiResponse<List<VariantResponse>>> getAllVariations(@PathVariable Long productId) {
        List<VariantResponse> responses = service.readAll(productId);
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get all product variations.")
    ResponseEntity<ApiResponse<VariantResponse>> getVariation(@PathVariable Long id, @PathVariable Long productId) {
        VariantResponse response = service.read(productId, id);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
