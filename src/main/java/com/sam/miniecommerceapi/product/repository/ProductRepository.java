package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT new com.sam.miniecommerceapi.product.dto.response.ProductResponse(" +
            "p.id, p.name, p.minPrice, p.slug, p.mainImage, c.name) " +
            "FROM Product p " +
            "JOIN p.category c")
    Page<ProductResponse> findAllSummary(Pageable pageable);

    @Query("SELECT new com.sam.miniecommerceapi.product.dto.response.ProductResponse(" +
            "p.id, p.name, p.minPrice, p.slug, p.mainImage, c.name) " +
            "FROM Product p " +
            "JOIN p.category c " +
            "WHERE (:keyword= '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ProductResponse> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT pv FROM ProductVariant pv " +
            "JOIN FETCH pv.product p " +
            "JOIN FETCH p.category " +
            "LEFT JOIN FETCH pv.options o " +
            "LEFT JOIN FETCH o.attribute " +
            "WHERE p.slug = :slug")
    List<ProductVariant> findBySlugWithDetails(@Param("slug") String slug);

    boolean existsBySlug(String slug);
}
