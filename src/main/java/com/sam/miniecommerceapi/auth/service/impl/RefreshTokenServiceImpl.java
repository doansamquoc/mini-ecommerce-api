package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.repository.RefreshTokenRepository;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
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
	AppProperties properties;
	RefreshTokenRepository repository;
	EntityManager manager;

	@Override
	public void validate(RefreshToken refreshToken) {
		if (refreshToken.isRevoked()) throw BusinessException.of(ErrorCode.TOKEN_REVOKED);
		if (refreshToken.isExpired()) throw BusinessException.of(ErrorCode.TOKEN_EXPIRED);
	}

	@Override
	public RefreshToken validateToken(Long userId) {
		RefreshToken token = findByUserId(userId);
		if (token.isExpired() || token.isRevoked()) {
			throw BusinessException.of(ErrorCode.TOKEN_EXPIRED);
		}
		return token;
	}

	@Override
	public RefreshToken validateToken(String refreshTokenStr) {
		RefreshToken token = findByToken(refreshTokenStr);
		if (token.isExpired() || token.isRevoked()) {
			throw BusinessException.of(ErrorCode.TOKEN_EXPIRED);
		}
		return token;
	}

	@Override
	public RefreshToken createToken(Long userId) {
		User userRef = manager.getReference(User.class, userId);
		RefreshToken refreshToken = RefreshToken.builder()
			.user(userRef)
			.token(UUID.randomUUID().toString())
			.expiresAt(Instant.now().plusMillis(properties.getRefreshTokenExpiration()))
			.revoked(false)
			.build();
		return repository.save(refreshToken);
	}

	@Override
	public void revoke(String token) {
		RefreshToken refreshToken = findByToken(token);
		revoke(refreshToken);
	}

	@Override
	public void revoke(Long userId) {
		RefreshToken refreshToken = findByUserId(userId);
		revoke(refreshToken);
	}


	@Override
	public void revoke(RefreshToken refreshToken) {
		refreshToken.revoke();
		repository.save(refreshToken);
	}

	@Override
	public void revokeAllByUserId(Long userId) {
		repository.revokeAllByUser(userId);
	}

	private RefreshToken findByToken(String token) {
		return repository.findByToken(token).orElseThrow(() -> BusinessException.of(ErrorCode.TOKEN_INVALID));
	}

	private RefreshToken findByUserId(Long userId) {
		return repository.findByUserId(userId).orElseThrow(() -> BusinessException.of(ErrorCode.TOKEN_INVALID));
	}
}
