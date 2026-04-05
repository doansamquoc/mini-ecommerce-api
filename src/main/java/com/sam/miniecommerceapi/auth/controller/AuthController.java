package com.sam.miniecommerceapi.auth.controller;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.response.AuthResponse;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.auth.service.*;
import com.sam.miniecommerceapi.shared.annotation.ClientIp;
import com.sam.miniecommerceapi.shared.annotation.UserAgent;
import com.sam.miniecommerceapi.shared.dto.response.ApiResponse;
import com.sam.miniecommerceapi.shared.service.CookieService;
import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    CookieService cookieService;
    PasswordService passwordService;
    IdentityService identityService;
    AuthenticationService authenticationService;
    TokenBlacklistService tokenBlacklistService;
    TokenManagementService tokenManagementService;

    @Operation(summary = "Login", description = "In program, the IP and UserAgent auto inject by annotation")
    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthResponse>> authenticate(
            @Valid @RequestBody LoginRequest loginRequest,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        TokenDTO token = authenticationService.login(loginRequest, ip, agent);
        String cookie = cookieService.createRefreshToken(token.getRefreshToken()).toString();
        AuthResponse authResponse = new AuthResponse(token.getAccessToken());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(ApiResponse.of(authResponse));
    }

    @PostMapping("/register")
    @Operation(summary = "Register User")
    ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserCreationRequest r) {
        UserResponse response = identityService.registerUser(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @Operation(
            summary = "Forgot password",
            description = "In program, the IP and UserAgent auto inject by annotation. Sent an email to reset password"
    )
    @PostMapping("/forgot-password")
    ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest r,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        passwordService.forgotPassword(r, ip, agent);
        return ResponseEntity.ok(ApiResponse.of());
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset password",
            description = "In program, the IP and UserAgent auto inject by annotation"
    )
    ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest r,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        passwordService.resetPassword(r, ip, agent);
        return ResponseEntity.ok(ApiResponse.of());
    }

    @Operation(summary = "Verify reset password token")
    @GetMapping("/verify-token")
    ResponseEntity<ApiResponse<String>> verifyPasswordResetToken(@RequestParam("token") String token) {
        passwordService.validateResetToken(token);
        return ResponseEntity.ok(ApiResponse.of());
    }

    @Operation(summary = "Refresh", description = "Refresh new access token")
    @GetMapping("/refresh")
    ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @CookieValue(name = "refresh-token", required = false) String refreshTokenStr,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        TokenDTO token = tokenManagementService.refreshAccessToken(refreshTokenStr, ip, agent);
        String cookie = cookieService.createRefreshToken(token.getRefreshToken()).toString();
        AuthResponse response = new AuthResponse(token.getAccessToken());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie).body(ApiResponse.of(response));
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    ResponseEntity<ApiResponse<String>> logout(@AuthenticationPrincipal UserPrincipal user) {
        long remainingTimeInMs = user.getExpiresAt().toEpochMilli() - System.currentTimeMillis();

        if (remainingTimeInMs > 0) {
            tokenBlacklistService.addToBlacklist(user.getJwtId(), remainingTimeInMs);
        }

        String cookie = cookieService.deleteRefreshToken().toString();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie).build();
    }

    // TODO: In the future add device checking (session) feature

}
