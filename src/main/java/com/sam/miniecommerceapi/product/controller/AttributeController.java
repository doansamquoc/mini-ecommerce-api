package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.product.dto.request.AttributeCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeResponse;
import com.sam.miniecommerceapi.product.service.AttributeService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product Attribute Endpoints.")
@RequestMapping("/api/v1/products/attributes")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeController {
    AttributeService service;

    @PostMapping
    @Operation(summary = "Create product attribute.")
    ResponseEntity<ApiResponse<AttributeResponse>> createAttribute(
            @Valid @RequestBody AttributeCreationRequest request
    ) {
        AttributeResponse response = service.createAttribute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PatchMapping("/{attributeId}")
    @Operation(summary = "Update product attribute.")
    ResponseEntity<ApiResponse<AttributeResponse>> updateAttribute(
            @PathVariable Long attributeId,
            @Valid @RequestBody AttributeUpdateRequest request
    ) {
        AttributeResponse response = service.updateAttribute(attributeId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping
    @Operation(summary = "Get all product attributes.")
    ResponseEntity<ApiResponse<List<AttributeResponse>>> getAttributes() {
        List<AttributeResponse> responses = service.getAllAttributes();
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @GetMapping("/{attributeId}")
    ResponseEntity<ApiResponse<AttributeResponse>> getAttributeById(@PathVariable Long attributeId) {
        AttributeResponse response = service.getAttribute(attributeId);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping
    ResponseEntity<ApiResponse<String>> deleteAllAttributes() {
        service.deleteAllAttributes();
        return ResponseEntity.ok(ApiResponse.of());
    }

    @DeleteMapping("/{attributeId}")
    ResponseEntity<ApiResponse<String>> deleteAttributeById(@PathVariable Long attributeId) {
        service.deleteAttribute(attributeId);
        return ResponseEntity.ok(ApiResponse.of());
    }
}
