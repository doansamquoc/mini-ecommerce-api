package com.sam.miniecommerceapi.auth.config;

import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.auth.security.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.common.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    JwtProvider jwtProvider;
    AppProperties appProperties;
    RefreshTokenService refreshTokenService;
    CookieService cookieService;

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Authentication authentication
    ) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createToken(
                userPrincipal.getId(), request.getRemoteAddr(), request.getHeader("User-Agent")
        );

        String targetUrl = UriComponentsBuilder
                .fromUriString(appProperties.getFrontendUrl() + "/oauth2/callback")
                .queryParam("token", accessToken)
                .build()
                .toUriString();

        ResponseCookie cookie = cookieService.createRefreshToken(refreshToken.getToken());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(targetUrl);
    }
}
