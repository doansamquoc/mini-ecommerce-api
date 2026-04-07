package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {
            "category",
            "variants",
            "variants.variantAttributes",
            "variants.variantAttributes.attribute"
    })
    Optional<Product> findBySlug(@Param("slug") String slug);

    @EntityGraph(attributePaths = {
            "category",
            "variants",
            "variants.variantAttributes",
            "variants.variantAttributes.attribute"
    })
    Optional<Product> findDetailsById(@Param("id") Long id);

    boolean existsBySlug(String slug);

    @Query("SELECT p.slug FROM Product p WHERE p.slug LIKE CONCAT(:baseSlug, '%')")
    List<String> findExistingSlugs(@Param("baseSlug") String baseSlug);
}
