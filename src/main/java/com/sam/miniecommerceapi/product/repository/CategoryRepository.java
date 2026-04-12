package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);

	Optional<Category> findBySlug(String slug);

	boolean existsBySlug(String slug);

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long id);

	boolean existsBySlugAndIdNot(String slug, Long id);
}
