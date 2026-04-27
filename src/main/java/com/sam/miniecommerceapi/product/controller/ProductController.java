package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.service.ProductSearchService;
import com.sam.miniecommerceapi.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product Endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
	EntityManager entityManager;
	ProductService productService;

	@PostMapping
	@Operation(summary = "Create a new product.")
	ResponseEntity<ApiResponse<ProductResponse>> createProduct(
		@Valid @RequestBody ProductCreationRequest request
	) {
		ProductResponse response = productService.insert(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@Operation(summary = "Update product by ID")
	@PatchMapping("/{id}")
	ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
		@PathVariable Long id,
		@Valid @RequestBody ProductUpdateRequest request
	) {
		ProductResponse response = productService.updateProduct(id, request);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/{slug}")
	@Operation(summary = "Get a product by slug.")
	ResponseEntity<ApiResponse<ProductDetailsResponse>> getProductBySlug(@PathVariable String slug) {
		ProductDetailsResponse response = productService.getProductDetailsBySlug(slug);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{id}")
	@Operation(
		summary = "Delete a product by ID.",
		description = "Soft delete. The product will not actually be deleted."
	)
	ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok(ApiResponse.of());
	}

	@DeleteMapping
	@Operation(
		summary = "Delete all products by ID.",
		description = "Soft delete. The products will not actually be deleted."
	)
	ResponseEntity<ApiResponse<String>> deleteAllProducts() {
		productService.deleteAllProduct();
		return ResponseEntity.ok(ApiResponse.of());
	}
}
