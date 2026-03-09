package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.auth.repository.PasswordResetTokenRepository;
import com.sam.miniecommerceapi.auth.service.PasswordResetTokenService;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.UuidUtils;
import com.sam.miniecommerceapi.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    AppProperties appProperties;
    PasswordResetTokenRepository repository;

    @Override
    public String createToken(User user) {
        String token = UuidUtils.generateUUID();
        Instant expiresAt = Instant.now().plusMillis(appProperties.getPasswordResetTokenExpiration());

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .isUsed(false)
                .user(user)
                .expiresAt(expiresAt)
                .build();

        repository.save(passwordResetToken);
        return token;
    }

    @Override
    public void deleteToken(User user) {
        repository.deleteByUser(user);
    }

    @Override
    public void deleteToken(String token) {
        repository.deleteByToken(token);
    }

    @Override
    public PasswordResetToken findByUser(User user) {
        return repository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_INVALID));
    }

    @Override
    public PasswordResetToken validateToken(String token) {
        PasswordResetToken passwordResetToken = findByToken(token);
        if (passwordResetToken.isExpired()) throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        return passwordResetToken;
    }
}
