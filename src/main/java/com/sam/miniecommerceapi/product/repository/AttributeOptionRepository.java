package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.AttributeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeOptionRepository extends JpaRepository<AttributeOption, Long> {
}
