package com.sam.miniecommerceapi.common.service;

import com.sam.miniecommerceapi.common.constant.AppConstant;
import com.sam.miniecommerceapi.common.config.AppProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CookieService {
    AppProperties app;

    public String extractRefreshToken(HttpServletRequest request) {
        return getCookie(request, AppConstant.REFRESH_TOKEN_COOKIE_NAME).map(Cookie::getValue).orElse(null);
    }

    private ResponseCookie build(String name, String value, long maxAge, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(app.isProduction())
                .sameSite("Lax")
                .maxAge(maxAge)
                .path(path)
                .build();
    }

    private ResponseCookie create(String name, String value, long maxAge, String path) {
        return build(name, value, maxAge, path);
    }

    public ResponseCookie createRefreshToken(String value) {
        long maxAgeInSeconds = app.getRefreshTokenExpiration() / 1000;
        return create(AppConstant.REFRESH_TOKEN_COOKIE_NAME, value, maxAgeInSeconds, "/api/v1/auth/refresh");
    }

    public ResponseCookie deleteRefreshToken() {
        return delete(AppConstant.REFRESH_TOKEN_COOKIE_NAME, "/api/v1/auth/refresh");
    }

    private ResponseCookie delete(String name, String path) {
        return build(name, null, 0, path);
    }

    public void addCookie(HttpServletResponse response, ResponseCookie cookie) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).findFirst();
    }
}
