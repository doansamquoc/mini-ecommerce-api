package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.AttributeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeDefinitionRepository extends JpaRepository<AttributeDefinition, Long> {
    boolean existsByCategoryIdAndAttributeKey(Long categoryId, String attributeKey);

    Optional<AttributeDefinition> findByAttributeKey(String attributeKey);

    void deleteByCategoryId(Long categoryId);

    List<AttributeDefinition> findAllByCategoryId(Long categoryId);

    @Query("SELECT ad.attributeKey FROM AttributeDefinition ad WHERE ad.category.id = :categoryId")
    List<String> findAttributeKeysByCategoryId(@Param("categoryId") Long categoryId);
}
