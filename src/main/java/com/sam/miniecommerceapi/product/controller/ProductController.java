package com.sam.miniecommerceapi.product.controller;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.service.ProductSearchService;
import com.sam.miniecommerceapi.product.service.ProductService;
import com.sam.miniecommerceapi.shared.dto.response.ApiResponse;
import com.sam.miniecommerceapi.shared.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product Endpoints.")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    EntityManager entityManager;
    ProductService productService;
    ProductSearchService searchService;

    @PostMapping("/reindex")
    @Operation(summary = "Reindex products for searching.")
    public ResponseEntity<?> reindex() throws InterruptedException {
        SearchSession searchSession = Search.session(entityManager);
        searchSession.massIndexer(Product.class).startAndWait();
        return ResponseEntity.ok("Indexing done");
    }

    @PostMapping
    @Operation(summary = "Create a new product.")
    ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreationRequest request
    ) {
        ProductResponse response = productService.createProduct(request);
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

    @GetMapping("/id/{id}")
    @Operation(summary = "Get a product by ID.")
    ResponseEntity<ApiResponse<ProductDetailsResponse>> getProductById(@PathVariable Long id) {
        ProductDetailsResponse response = productService.getProductDetailsById(id);
        return ResponseEntity.ok(ApiResponse.of(response));
    }

    @Operation(summary = "Search products.", description = "Hibernate Search Engine (Lucene).")
    @GetMapping("/search")
    ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        PageResponse<ProductResponse> responses = searchService.searchProducts(q, minPrice, categoryName, pageable);
        return ResponseEntity.ok(ApiResponse.of(responses));
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
