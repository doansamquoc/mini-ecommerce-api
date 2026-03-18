package com.sam.miniecommerceapi.cart.repository;

import com.sam.miniecommerceapi.cart.entity.Cart;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.variant v WHERE c.user.id = :id")
    Page<Cart> findAllByUserId(Long id, Pageable pageable);

    Optional<Cart> findByUserAndVariant(User user, ProductVariant productVariant);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.id = :cartId AND c.user.id = :userId")
    void deleteCart(@Param("cartId") Long cartId, @Param("userId") Long userId);

    void deleteCartByUserId(Long userId);
}
