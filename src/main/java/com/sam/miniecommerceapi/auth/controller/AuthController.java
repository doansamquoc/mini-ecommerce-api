package com.sam.miniecommerceapi.auth.controller;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.response.AuthResponse;
import com.sam.miniecommerceapi.auth.service.AuthenticationService;
import com.sam.miniecommerceapi.auth.service.IdentityService;
import com.sam.miniecommerceapi.auth.service.PasswordService;
import com.sam.miniecommerceapi.auth.service.TokenManagementService;
import com.sam.miniecommerceapi.common.annotation.ClientIp;
import com.sam.miniecommerceapi.common.annotation.UserAgent;
import com.sam.miniecommerceapi.common.api.SuccessApi;
import com.sam.miniecommerceapi.common.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.service.CookieService;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    CookieService cookieService;
    PasswordService passwordService;
    IdentityService identityService;
    AuthenticationService authenticationService;
    TokenManagementService tokenManagementService;

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
    ResponseEntity<SuccessApi<UserResponse>> register(@Valid @RequestBody UserCreationRequest r) {
        UserResponse userResponse = identityService.createUser(r);
        return ApiFactory.created(userResponse, "User has been created");
    }

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
    ResponseEntity<SuccessApi<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest r,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        passwordService.resetPassword(r, ip, agent);
        return ApiFactory.success("Your password has been changed!");
    }

    @GetMapping("/verify-token")
    ResponseEntity<SuccessApi<String>> verifyPasswordResetToken(@RequestParam("token") String token) {
        passwordService.validateResetToken(token);
        return ApiFactory.success("Verify token successfully!");
    }

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
}
