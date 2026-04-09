package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.user.entity.User;

public interface RefreshTokenService {

	void validate(RefreshToken refreshToken);

	RefreshToken validateToken(Long userId);

	RefreshToken validateToken(String refreshTokenStr);

	RefreshToken createToken(Long userId);

	void revoke(String token);

	void revoke(Long userId);

	void revoke(RefreshToken refreshToken);

	void revokeAllByUserId(Long userId);
}
