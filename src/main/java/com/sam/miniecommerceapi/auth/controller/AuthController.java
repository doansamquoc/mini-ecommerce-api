package com.sam.miniecommerceapi.auth.controller;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.CreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
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
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    CookieService cookieService;

    @PostMapping("/login")
    ResponseEntity<SuccessApi<AuthResponse>> authenticate(
            @Valid @RequestBody LoginRequest loginRequest,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        LoginResult result = authService.login(loginRequest, ip, agent);
        ResponseCookie cookie = cookieService.createRefreshToken(result.getRefreshToken());
        AuthResponse authResponse = new AuthResponse(result.getAccessToken());

        return ApiFactory.success(cookie.toString(), authResponse, "Login successfully!");
    }

    @PostMapping("/register")
    ResponseEntity<SuccessApi<UserResponse>> register(@Valid @RequestBody CreationRequest r) {
        UserResponse userResponse = authService.register(r);
        return ApiFactory.created(userResponse, "User has been created");
    }

    @PostMapping("/forgot-password")
    ResponseEntity<SuccessApi<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest r,
            @ClientIp String ip,
            @UserAgent String agent
    ) {
        authService.forgotPassword(r, ip, agent);
        return ApiFactory.success("We have sent a message to " + r.getEmail() + ". Please check your mailbox!");
    }

    @PostMapping("/reset-password")
    ResponseEntity<SuccessApi<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest r) {
        authService.resetPassword(r);
        return ApiFactory.success("Your password has been changed!");
    }

    @GetMapping("/verify-token")
    ResponseEntity<SuccessApi<String>> verifyPasswordResetToken(@RequestParam("token") String token)  {
        authService.validateToken(token);
        return ApiFactory.success("Verify token successfully!");
    }
}
