package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.user.entity.User;

public interface RefreshTokenService {
    void validate(RefreshToken refreshToken);

    RefreshToken validateToken(User user);

    RefreshToken validateToken(String refreshTokenStr);

    RefreshToken createToken(User user, String ip, String agent);

    void revoke(RefreshToken refreshToken);

    void revoke(String token);

    void revoke(User user);

    void revokeAllByUser(User user);
}
