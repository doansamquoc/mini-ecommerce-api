package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.dto.response.ProductSummaryResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT new com.sam.miniecommerceapi.product.dto.response.ProductSummaryResponse(p.id, p.name, p.slug, p.mainImage, c.name, MIN(v.price)) " +
            "FROM Product p " +
            "JOIN Category c " +
            "JOIN ProductVariant v ON v.product.id = p.id " +
            "GROUP BY p.id, p.name, p.slug, p.mainImage, c.name"
    )
    Page<ProductSummaryResponse> findAllWithMinPrice(Pageable pageable);

    @Query("SELECT pv FROM ProductVariant pv " +
            "JOIN FETCH pv.product p " +
            "JOIN FETCH p.category " +
            "LEFT JOIN FETCH pv.options o " +
            "LEFT JOIN FETCH o.attribute " +
            "WHERE p.slug = :slug")
    List<ProductVariant> findBySlugWithDetails(@Param("slug") String slug);

    boolean existsBySlug(String slug);
}
