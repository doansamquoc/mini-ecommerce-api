package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByNameIgnoreCase(String name);
}
