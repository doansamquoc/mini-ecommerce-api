package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.security.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.auth.service.TokenManagementService;
import com.sam.miniecommerceapi.common.dto.UserPrincipal;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenManagementServiceImpl implements TokenManagementService {
    JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public TokenDTO refreshAccessToken(String refreshTokenStr, String ip, String agent) {
        if (refreshTokenStr == null) throw new BusinessException(ErrorCode.TOKEN_INVALID);

        RefreshToken oldRefreshToken = refreshTokenService.validateToken(refreshTokenStr);
        User user = oldRefreshToken.getUser();

        refreshTokenService.revoke(oldRefreshToken);

        return enrichToken(user, ip, agent);
    }

    private TokenDTO enrichToken(User user, String ip, String agent) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        String accessToken = jwtProvider.generateAccessToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createToken(user, ip, agent);

        return new TokenDTO(accessToken, refreshToken.getToken());
    }
}
