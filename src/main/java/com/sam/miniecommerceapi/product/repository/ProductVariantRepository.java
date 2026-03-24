package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    boolean existsBySku(String sku);

    @Query("SELECT v.sku FROM ProductVariant v WHERE v.sku IN :skus")
    List<String> findExistingSkus(Collection<String> skus);

    List<ProductVariant> findAllByProductId(Long productId);

    /**
     * @param id       Product variant ID
     * @param quantity Product quantity
     * @return The record number has been updated
     */
    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant v " +
            "SET v.stockQuantity = v.stockQuantity - :quantity " +
            "WHERE v.id = :id AND v.stockQuantity >= :quantity")
    int deductStock(@Param("id") long id, @Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE ProductVariant v " +
            "SET v.stockQuantity = v.stockQuantity + :quantity " +
            "WHERE v.id = :id")
    void addStock(@Param("id") long id, @Param("quantity") int quantity);
}
