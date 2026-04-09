package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;

public interface PasswordService {
	void forgotPassword(ForgotPasswordRequest req);

	void resetPassword(ResetPasswordRequest req);

	void validateResetToken(String resetTokenStr);
}
