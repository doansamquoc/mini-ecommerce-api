package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeDefinitionUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeDefinitionResponse;
import com.sam.miniecommerceapi.product.service.AttributeDefinitionService;
import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/attribute-definitions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeDefinitionController {
    AttributeDefinitionService service;

    @PostMapping
    @Operation(summary = "Create a new attribute definition.")
    ResponseEntity<ApiResponse<AttributeDefinitionResponse>> createAttribute(
            @Valid @RequestBody AttributeDefinitionCreationRequest request
    ) {
        AttributeDefinitionResponse response = service.createAttribute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Make changes a attribute definition.")
    ResponseEntity<ApiResponse<AttributeDefinitionResponse>> updateAttribute(
            @PathVariable Long id,
            @Valid @RequestBody AttributeDefinitionUpdateRequest request
    ) {
        AttributeDefinitionResponse response = service.updateAttribute(id, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping()
    @Operation(summary = "Get all attribute definitions.")
    ResponseEntity<ApiResponse<List<AttributeDefinitionResponse>>> getAllAttributes() {
        List<AttributeDefinitionResponse> responses = service.getAllAttributes();
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all attribute definitions by category ID.")
    ResponseEntity<ApiResponse<List<AttributeDefinitionResponse>>> getAllAttributesByCategoryId(
            @PathVariable Long categoryId
    ) {
        List<AttributeDefinitionResponse> responses = service.getAllByCategoryId(categoryId);
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a attribute definition by ID.")
    ResponseEntity<ApiResponse<AttributeDefinitionResponse>> getAttributeById(@PathVariable Long id) {
        AttributeDefinitionResponse response = service.getById(id);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping("/attribute/{attributeKey}")
    @Operation(summary = "Get all attribute definitions by attribute key.")
    ResponseEntity<ApiResponse<AttributeDefinitionResponse>> getAttributeByAttributeKey(
            @PathVariable String attributeKey
    ) {
        AttributeDefinitionResponse response = service.getByAttributeKey(attributeKey);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
