package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;

public interface PasswordService {
    void forgotPassword(ForgotPasswordRequest r, String ip, String agent);

    void resetPassword(ResetPasswordRequest r, String ip, String agent);

    PasswordResetToken validateResetToken(String resetTokenStr);
}
