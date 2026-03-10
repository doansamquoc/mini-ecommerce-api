package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.CreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    LoginResult login(LoginRequest request, String ip, String agent);

    UserResponse register(CreationRequest r);

    void forgotPassword(ForgotPasswordRequest r, String ip, String agent);

    void resetPassword(ResetPasswordRequest r);

    PasswordResetToken validateToken(String token);

    @Transactional
    LoginResult refresh(String tokenString, String ip, String agent);
}
