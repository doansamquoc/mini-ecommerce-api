package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.sam.miniecommerceapi.product.dto.response.ProductResponse(" +
            "p.id, p.name, p.minPrice, p.slug, p.imageUrl, c.name) " +
            "FROM Product p JOIN p.category c")
    Page<ProductResponse> findAllSummary(Pageable pageable);

    @Query("SELECT new com.sam.miniecommerceapi.product.dto.response.ProductResponse" +
            "(p.id, p.name, p.minPrice, p.slug, p.imageUrl, c.name) " +
            "FROM Product p JOIN p.category c " +
            "WHERE (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ProductResponse> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"variants", "variants.values", "variants.values.attribute"})
    Optional<Product> findBySlug(@Param("slug") String slug);

    @EntityGraph(attributePaths = {"variants", "variants.values", "variants.values.attribute"})
    Optional<Product> findDetailsById(@Param("id") Long id);

    boolean existsBySlug(String slug);

    @Query("SELECT p.slug FROM Product p WHERE p.slug LIKE CONCAT(:baseSlug, '%')")
    List<String> findExistingSlugs(@Param("baseSlug") String baseSlug);
}
