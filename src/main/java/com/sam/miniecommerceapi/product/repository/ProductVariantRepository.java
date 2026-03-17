package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    boolean existsBySku(String sku);

    @Query("SELECT v.sku FROM ProductVariant v WHERE v.sku IN :skus")
    List<String> findExistingSkus(Collection<String> skus);
}
