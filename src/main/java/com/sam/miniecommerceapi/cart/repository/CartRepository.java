package com.sam.miniecommerceapi.cart.repository;

import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	@Modifying
	@Query("DELETE FROM Cart c WHERE c.id = :cartId AND c.user.id = :userId")
	void deleteCart(@Param("cartId") Long cartId, @Param("userId") Long userId);

	void deleteCartByUserId(Long userId);

	boolean existsByUserId(Long userId);

	@EntityGraph(attributePaths = {"cartItems", "cartItems.variant", "cartItems.variant.product"})
	@Query("SELECT DISTINCT c FROM Cart c WHERE c.user.id = :userId")
	Optional<Cart> findFullByUserId(@Param("userId") Long userId);

	Long user(User user);
}
