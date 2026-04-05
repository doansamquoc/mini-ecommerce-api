package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.repository.RefreshTokenRepository;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import com.sam.miniecommerceapi.config.AppProperties;
import com.sam.miniecommerceapi.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    AppProperties appProperties;
    RefreshTokenRepository repository;
    EntityManager entityManager;

    @Override
    public void validate(RefreshToken refreshToken) {
        if (refreshToken.isRevoked())
            throw new BusinessException(ErrorCode.TOKEN_REVOKED);
        if (refreshToken.isExpired())
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
    }

    @Override
    public RefreshToken validateToken(User user) {
        RefreshToken token = findByUser(user);
        if (token.isExpired() || token.isRevoked()) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        }
        return token;
    }

    @Override
    public RefreshToken validateToken(String refreshTokenStr) {
        RefreshToken token = findByToken(refreshTokenStr);
        if (token.isExpired() || token.isRevoked()) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        }
        return token;
    }

    @Override
    public RefreshToken createToken(Long userId, String ip, String agent) {
        User userRef = entityManager.getReference(User.class, userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRef)
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plusMillis(appProperties.getRefreshTokenExpiration()))
                .revoked(false)
                .ip(ip)
                .device(agent)
                .build();
        return repository.save(refreshToken);
    }

    @Override
    public void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        repository.save(refreshToken);
    }

    @Override
    public void revoke(String token) {
        RefreshToken refreshToken = findByToken(token);
        revoke(refreshToken);
    }

    @Override
    public void revoke(User user) {
        RefreshToken refreshToken = findByUser(user);
        revoke(refreshToken);
    }

    @Override
    public void revokeAllByUser(User user) {
        repository.revokeAllByUser(user);
    }

    private RefreshToken findByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_INVALID));
    }

    private RefreshToken findByUser(User user) {
        return repository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_INVALID));
    }
}
