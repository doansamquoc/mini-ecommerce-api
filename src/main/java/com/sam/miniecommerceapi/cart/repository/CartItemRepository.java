package com.sam.miniecommerceapi.cart.repository;

import com.sam.miniecommerceapi.cart.entity.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartId(Long cartId);

	@EntityGraph(attributePaths = {"cart", "variant", "variant.product"})
	@Query("SELECT ci FROM CartItem ci WHERE ci.cart.user.id = :userId AND ci.variant.id = :variantId")
	Optional<CartItem> findByUserIdAndVariantId(@Param("userId") Long userId, @Param("variantId") Long variantId);
}
