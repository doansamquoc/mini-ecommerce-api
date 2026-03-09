package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.user.entity.User;
import org.springframework.stereotype.Service;

public interface PasswordResetTokenService {
    String createToken(User user);

    void deleteToken(User user);

    void deleteToken(String token);

    PasswordResetToken findByUser(User user);

    PasswordResetToken findByToken(String token);

    PasswordResetToken validateToken(String token);
}
