package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findBySlug(String slug);

    Optional<Category> findByName(String name);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);
}
