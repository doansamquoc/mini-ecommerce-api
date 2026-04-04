package com.sam.miniecommerceapi.product.controller.rest;

import com.sam.miniecommerceapi.product.dto.request.ProductCreationRequest;
import com.sam.miniecommerceapi.product.dto.request.ProductUpdateRequest;
import com.sam.miniecommerceapi.product.dto.response.ProductDetailsResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.service.ProductSearchService;
import com.sam.miniecommerceapi.product.service.ProductService;
import com.sam.miniecommerceapi.shared.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.shared.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    EntityManager entityManager;
    ProductService productService;
    ProductSearchService searchService;

    @PostMapping("/reindex")
    public ResponseEntity<?> reindex() throws InterruptedException {
        SearchSession searchSession = Search.session(entityManager);
        searchSession.massIndexer(Product.class).startAndWait();
        return ResponseEntity.ok("Indexing done");
    }

    @Operation(summary = "Create product")
    @PostMapping
    ResponseEntity<SuccessApi<ProductDetailsResponse>> createProduct(@Valid @RequestBody ProductCreationRequest r) {
        ProductDetailsResponse response = productService.createProduct(r);
        return ApiFactory.success(response, "Create product successfully.");
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get product by slug")
    ResponseEntity<SuccessApi<ProductDetailsResponse>> getProductBySlug(@PathVariable String slug) {
        ProductDetailsResponse response = productService.getProductBySlug(slug);
        return ApiFactory.success(response, "Get product successfully.");
    }

    @Operation(summary = "Search products", description = "Hibernate Search Engine (Lucene)")
    @GetMapping("/search")
    ResponseEntity<SuccessApi<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        PageResponse<ProductResponse> responses = searchService.searchProducts(keyword, minPrice, categoryName, pageable);
        return ApiFactory.success(responses, "Success");
    }

    @Operation(summary = "Update product by ID")
    @PutMapping("/{id}")
    ResponseEntity<SuccessApi<ProductDetailsResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest r
    ) {
        ProductDetailsResponse response = productService.updateProduct(id, r);
        return ApiFactory.success(response, "Update product successfully.");
    }
}
