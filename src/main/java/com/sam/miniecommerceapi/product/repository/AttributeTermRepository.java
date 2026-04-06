package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.AttributeTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeTermRepository extends JpaRepository<AttributeTerm, Long> {
    Optional<AttributeTerm> findByAttributeIdAndId(Long attributeId, Long id);

    Optional<AttributeTerm> findByAttributeId(Long attributeId);

    List<AttributeTerm> findAllByAttributeId(Long attributeId);

    boolean existsByNameIgnoreCase(String name);

    void deleteByAttributeIdAndId(Long attributeId, Long id);
    void deleteAllByAttributeId(Long attributeId);
    void deleteAll();
}
