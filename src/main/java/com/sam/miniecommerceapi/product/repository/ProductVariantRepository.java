package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
	boolean existsBySku(String sku);

	@Query("SELECT v.sku FROM ProductVariant v WHERE v.sku IN :skus")
	List<String> findExistingSkus(Collection<String> skus);

	List<ProductVariant> findAllByProductId(Long productId);

	Optional<ProductVariant> findByProductIdAndId(Long productId, Long id);

	@Transactional
	@Modifying
	@Query("UPDATE ProductVariant v SET v.stock = v.stock - :quantity WHERE v.id = :id AND v.stock >= :quantity")
	int deductStock(@Param("id") long id, @Param("quantity") int quantity);

	@Modifying
	@Query("UPDATE ProductVariant v SET v.stock = v.stock + :quantity WHERE v.id = :id")
	void increaseStock(@Param("id") long id, @Param("quantity") int quantity);

	Optional<ProductVariant> findByProductAndId(Product product, Long id);

	void deleteByProductIdAndId(Long productId, Long id);

	void deleteAllByProductId(Long productId);

	@EntityGraph(attributePaths = {"product"})
	@Query("SELECT v FROM ProductVariant v WHERE v.id = :id")
	Optional<ProductVariant> findGraphById(Long id);


	@Query("SELECT v.sku FROM ProductVariant v WHERE v.sku IN :skus")
	List<String> findExistingSkus(List<String> skus);
}
