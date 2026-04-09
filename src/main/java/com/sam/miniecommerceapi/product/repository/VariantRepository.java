package com.sam.miniecommerceapi.product.repository;

import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.entity.Variant;
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
public interface VariantRepository extends JpaRepository<Variant, Long> {
	boolean existsBySku(String sku);

	@Query("SELECT v.sku FROM Variant v WHERE v.sku IN :skus")
	List<String> findExistingSkus(Collection<String> skus);

	List<Variant> findAllByProductId(Long productId);

	Optional<Variant> findByProductIdAndId(Long productId, Long id);

	@Transactional
	@Modifying
	@Query("UPDATE Variant v SET v.stockQuantity = v.stockQuantity - :quantity WHERE v.id = :id AND v.stockQuantity >= :quantity")
	int deductStock(@Param("id") long id, @Param("quantity") int quantity);

	@Modifying
	@Query("UPDATE Variant v SET v.stockQuantity = v.stockQuantity + :quantity WHERE v.id = :id")
	void increaseStock(@Param("id") long id, @Param("quantity") int quantity);

	Optional<Variant> findByProductAndId(Product product, Long id);

	void deleteByProductIdAndId(Long productId, Long id);

	void deleteAllByProductId(Long productId);

	@EntityGraph(attributePaths = {"product"})
	@Query("SELECT v FROM Variant v WHERE v.id = :id")
	Optional<Variant> findGraphById(Long id);
}
