package com.sam.miniecommerceapi.auth.controller;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.auth.service.*;
import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.response.AuthResponse;
import com.sam.miniecommerceapi.common.annotation.ClientIp;
import com.sam.miniecommerceapi.common.annotation.UserAgent;
import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.service.CookieService;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

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
    ResponseEntity<SuccessApi<AuthResponse>> authenticate(
            @Valid @RequestBody LoginRequest loginRequest,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        TokenDTO token = authenticationService.login(loginRequest, ip, agent);
        ResponseCookie cookie = cookieService.createRefreshToken(token.getRefreshToken());
        AuthResponse authResponse = new AuthResponse(token.getAccessToken());

        return ApiFactory.success(cookie.toString(), authResponse, "Login successfully!");
    }

    @PostMapping("/register")
    @Operation(summary = "Register User")
    ResponseEntity<SuccessApi<UserResponse>> register(@Valid @RequestBody UserCreationRequest r) {
        UserResponse userResponse = identityService.registerUser(r);
        return ApiFactory.created(userResponse, "User has been created");
    }

    @Operation(
            summary = "Forgot password",
            description = "In program, the IP and UserAgent auto inject by annotation. Sent an email to reset password"
    )
    @PostMapping("/forgot-password")
    ResponseEntity<SuccessApi<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest r,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        passwordService.forgotPassword(r, ip, agent);
        return ApiFactory.success("We have sent a message to " + r.getEmail() + ". Please check your mailbox!");
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset password",
            description = "In program, the IP and UserAgent auto inject by annotation"
    )
    ResponseEntity<SuccessApi<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest r,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        passwordService.resetPassword(r, ip, agent);
        return ApiFactory.success("Your password has been changed!");
    }

    @Operation(summary = "Verify reset password token")
    @GetMapping("/verify-token")
    ResponseEntity<SuccessApi<String>> verifyPasswordResetToken(@RequestParam("token") String token) {
        passwordService.validateResetToken(token);
        return ApiFactory.success("Verify token successfully!");
    }

    @Operation(summary = "Refresh", description = "Refresh new access token")
    @GetMapping("/refresh")
    ResponseEntity<SuccessApi<AuthResponse>> refresh(
            @CookieValue(name = "refresh-token", required = false) String refreshTokenStr,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        TokenDTO token = tokenManagementService.refreshAccessToken(refreshTokenStr, ip, agent);
        ResponseCookie cookie = cookieService.createRefreshToken(token.getRefreshToken());
        AuthResponse response = new AuthResponse(token.getAccessToken());

        return ApiFactory.success(cookie.toString(), response, "Refresh access token successfully.");
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    ResponseEntity<SuccessApi<String>> logout(@AuthenticationPrincipal UserPrincipal user) {
        long remainingTimeInMs = user.getExpiresAt().toEpochMilli() - System.currentTimeMillis();

        if (remainingTimeInMs > 0) {
            tokenBlacklistService.addToBlacklist(user.getJwtId(), remainingTimeInMs);
        }

        ResponseCookie cookie = cookieService.deleteRefreshToken();
        return ApiFactory.success(cookie.toString(), null, "Logged out successfully");
    }

    // TODO: In the future add device checking (session) feature

}
