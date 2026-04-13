package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	@EntityGraph(attributePaths = {"category", "variants", "image"})
	Optional<Product> findBySlug(@Param("slug") String slug);

	boolean existsBySlug(String slug);
}
