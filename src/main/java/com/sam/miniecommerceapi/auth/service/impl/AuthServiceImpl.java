package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.security.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.service.AuthService;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.dto.UserPrincipal;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserService userService;
    AuthenticationManager authManager;
    JwtProvider jwtProvider;
    RefreshTokenService refreshTokenService;

    private UserPrincipal authenticate(String identifier, String password) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
            return (UserPrincipal) auth.getPrincipal();
        } catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    @Override
    public LoginResult login(LoginRequest request, String ip, String agent) {
        UserPrincipal userPrincipal = authenticate(request.getIdentifier(), request.getPassword());

        String accessToken = jwtProvider.generateAccessToken(userPrincipal);

        User user = userService.getReference(userPrincipal.getId());
        RefreshToken refreshToken = refreshTokenService.create(user, ip, agent);

        return LoginResult.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).build();
    }
}
