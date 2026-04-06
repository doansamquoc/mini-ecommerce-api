package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.product.dto.request.AttributeTermCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.AttributeTermUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.AttributeTermResponse;
import com.sam.miniecommerceapi.product.service.AttributeTermService;
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
@Tag(name = "Product Attribute Term APIs.")
@RequestMapping("/api/v1/products/attributes/{attributeId}/terms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeTermController {
    AttributeTermService service;

    @PostMapping
    @Operation(summary = "Create a new product attribute term.")
    ResponseEntity<ApiResponse<AttributeTermResponse>> createAttributeTerm(
            @PathVariable Long attributeId,
            @Valid @RequestBody AttributeTermCreationRequest request
    ) {
        AttributeTermResponse response = service.createTerm(attributeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PatchMapping("/{attributeTermId}")
    @Operation(summary = "Make changes to a product attribute term.")
    ResponseEntity<ApiResponse<AttributeTermResponse>> updateTerms(
            @PathVariable Long attributeId,
            @PathVariable Long attributeTermId,
            @Valid @RequestBody AttributeTermUpdateRequest request
    ) {
        AttributeTermResponse response = service.updateTerm(attributeId, attributeTermId, request);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @GetMapping
    @Operation(summary = "Get all terms from a product attribute.")
    ResponseEntity<ApiResponse<List<AttributeTermResponse>>> getAllTerms(@PathVariable Long attributeId) {
        List<AttributeTermResponse> responses = service.getTerms(attributeId);
        return ResponseEntity.ok(ApiResponse.of(responses));
    }

    @GetMapping("/{attributeTermId}")
    @Operation(summary = "Get a product attribute term.")
    ResponseEntity<ApiResponse<AttributeTermResponse>> getTerm(
            @PathVariable Long attributeId,
            @PathVariable Long attributeTermId
    ) {
        AttributeTermResponse response = service.getTerm(attributeId, attributeTermId);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @DeleteMapping("/{attributeTermId}")
    @Operation(summary = "Delete a product attribute term.")
    ResponseEntity<ApiResponse<String>> deleteTerm(
            @PathVariable Long attributeId,
            @PathVariable Long attributeTermId
    ) {
        service.deleteTerm(attributeId, attributeTermId);
        return ResponseEntity.ok(ApiResponse.of());
    }

    @DeleteMapping
    @Operation(summary = "Delete all terms from a product attribute.")
    ResponseEntity<ApiResponse<String>> deleteAllTerms(@PathVariable Long attributeId) {
        service.deleteAllTermsByAttributeId(attributeId);
        return ResponseEntity.ok(ApiResponse.of());
    }
}
