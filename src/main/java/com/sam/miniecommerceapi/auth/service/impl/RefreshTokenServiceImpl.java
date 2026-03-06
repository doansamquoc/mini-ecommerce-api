package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.repository.RefreshTokenRepository;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.user.entity.User;
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

    @Override
    public void validate(RefreshToken refreshToken) {
        if (refreshToken.isRevoked())
            throw new BusinessException(ErrorCode.TOKEN_REVOKED);
        if (refreshToken.getExpiresAt().isBefore(Instant.now()))
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
    }

    @Override
    public RefreshToken create(User user, String ip, String agent) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
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
        return repository.findByToken(token).orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
    }

    private RefreshToken findByUser(User user) {
        return repository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
    }
}
