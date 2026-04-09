package com.sam.miniecommerceapi.auth.repository;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUserId(Long userId);

	@Modifying
	@Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user.id = :userId AND r.revoked = false")
	void revokeAllByUser(Long userId);

	Long user(User user);
}
