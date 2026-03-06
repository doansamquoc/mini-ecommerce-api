package com.sam.miniecommerceapi.auth.controller;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.CreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.response.AuthResponse;
import com.sam.miniecommerceapi.auth.service.AuthService;
import com.sam.miniecommerceapi.common.annotation.ClientIp;
import com.sam.miniecommerceapi.common.annotation.UserAgent;
import com.sam.miniecommerceapi.common.api.SuccessApi;
import com.sam.miniecommerceapi.common.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.service.CookieService;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    CookieService cookieService;

    @PostMapping("/login")
    ResponseEntity<SuccessApi<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            @ClientIp String ip, @UserAgent String agent
    ) {
        LoginResult result = authService.login(loginRequest, ip, agent);
        ResponseCookie cookie = cookieService.createRefreshToken(result.getRefreshToken());
        AuthResponse authResponse = new AuthResponse(result.getAccessToken());
        return ApiFactory.success(cookie.toString(), authResponse, "Login successfully!");
    }

    @PostMapping("/create")
    ResponseEntity<SuccessApi<UserResponse>> create(@Valid @RequestBody CreationRequest r) {
        UserResponse userResponse = authService.create(r);
        return ApiFactory.created(userResponse, "User has been created");
    }
}
