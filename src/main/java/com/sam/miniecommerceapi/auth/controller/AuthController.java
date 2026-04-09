package com.sam.miniecommerceapi.auth.controller;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.response.AuthResponse;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.auth.service.*;
import com.sam.miniecommerceapi.common.constant.AppConstant;
import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import com.sam.miniecommerceapi.common.util.CookieUtils;
import com.sam.miniecommerceapi.config.AppProperties;
import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
	AppProperties properties;
	PasswordService passwordService;
	IdentityService identityService;
	AuthenticationService authenticationService;
	TokenBlacklistService tokenBlacklistService;
	TokenManagementService tokenManagementService;
	RefreshTokenService tokenService;

	@PostMapping("/login")
	@Operation(summary = "Login")
	ResponseEntity<ApiResponse<?>> authenticate(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse res) {
		TokenDTO token = authenticationService.login(loginRequest);
		long refreshTokenMaxAgeInSeconds = properties.getRefreshTokenExpiration() / 1000;
		long accessTokenMaxAgeInSeconds = properties.getAccessTokenExpiration() / 1000;

		CookieUtils.addRefreshToken(res, token.refreshToken(), refreshTokenMaxAgeInSeconds);
		CookieUtils.addAccessToken(res, token.accessToken(), accessTokenMaxAgeInSeconds);
		return ResponseEntity.ok(ApiResponse.of());
	}

	@PostMapping("/register")
	@Operation(summary = "Register a new user")
	ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserCreationRequest r) {
		UserResponse response = identityService.registerUser(r);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@PostMapping("/forgot-password")
	@Operation(summary = "Forgot password")
	ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
		passwordService.forgotPassword(req);
		return ResponseEntity.ok(ApiResponse.of());
	}

	@PostMapping("/reset-password")
	@Operation(summary = "Reset password")
	ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
		passwordService.resetPassword(req);
		return ResponseEntity.ok(ApiResponse.of());
	}

	@GetMapping("/verify-token")
	@Operation(summary = "Verify reset password token.")
	ResponseEntity<ApiResponse<String>> verifyPasswordResetToken(@RequestParam("token") String token) {
		passwordService.validateResetToken(token);
		return ResponseEntity.ok(ApiResponse.of());
	}

	@GetMapping("/refresh")
	@Operation(summary = "Refresh Token.", description = "Refresh new refresh, access token.")
	ResponseEntity<ApiResponse<AuthResponse>> refresh(
		HttpServletResponse res,
		@CookieValue(name = AppConstant.REFRESH_TOKEN_COOKIE_NAME, required = false) String tokenStr
	) {
		TokenDTO token = tokenManagementService.refreshToken(tokenStr);
		long refreshTokenMaxAgeInSeconds = properties.getRefreshTokenExpiration() / 1000;
		long accessTokenMaxAgeInSeconds = properties.getAccessTokenExpiration() / 1000;

		CookieUtils.addRefreshToken(res, token.refreshToken(), refreshTokenMaxAgeInSeconds);
		CookieUtils.addAccessToken(res, token.accessToken(), accessTokenMaxAgeInSeconds);
		return ResponseEntity.ok(ApiResponse.of());
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout")
	ResponseEntity<ApiResponse<String>> logout(
		HttpServletResponse res,
		@AuthenticationPrincipal UserPrincipal user,
		@CookieValue(name = AppConstant.REFRESH_TOKEN_COOKIE_NAME, required = false) String tokenStr
	) {
		long remainingTimeInMs = user.getExpiresAt().toEpochMilli() - System.currentTimeMillis();
		if (remainingTimeInMs > 0) {
			tokenBlacklistService.addToBlacklist(user.getJwtId(), remainingTimeInMs);
		}

		tokenService.revoke(tokenStr);
		CookieUtils.deleteRefreshToken(res);
		CookieUtils.deleteAccessToken(res);

		return ResponseEntity.ok(ApiResponse.of());
	}
}
