package com.sam.miniecommerceapi.auth.repository;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    List<RefreshToken> findByUserAndRevokedFalseAndExpiresAtAfter(User user, Instant instant);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user AND r.revoked = false")
    void revokeAllByUser(User user);

    @Modifying
    @Query("SELECT r FROM RefreshToken r WHERE r.user = :user AND r.revoked = false")
    Optional<RefreshToken> queryByUser(User user);
}
