package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.auth.service.PasswordResetTokenService;
import com.sam.miniecommerceapi.auth.service.PasswordService;
import com.sam.miniecommerceapi.config.AppProperties;
import com.sam.miniecommerceapi.event.PasswordChangedEvent;
import com.sam.miniecommerceapi.event.PasswordResetEvent;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordServiceImpl implements PasswordService {
    UserService userService;
    AppProperties app;
    PasswordEncoder encoder;
    ApplicationEventPublisher publisher;
    PasswordResetTokenService passwordResetTokenService;

    @Override
    public void forgotPassword(ForgotPasswordRequest req) {
        userService.findOptionalByEmail(req.getEmail()).ifPresent(user -> {
            PasswordResetToken passwordResetToken = passwordResetTokenService.createToken(user);
            passwordResetTokenService.createToken(user);
            publishForgotPasswordMessage(user, passwordResetToken);
        });
    }

    @Override
    public void resetPassword(ResetPasswordRequest req) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(req.getToken());
        User user = passwordResetToken.getUser();

        String hashPassword = encoder.encode(req.getNewPassword());

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().password(hashPassword).build();
        userService.updateUser(user.getId(), userUpdateRequest);

        publishPasswordChangedMessage(user);
    }

    @Override
    public void validateResetToken(String resetTokenStr) {
	    passwordResetTokenService.validateToken(resetTokenStr);
    }

    private void publishForgotPasswordMessage(User user, PasswordResetToken passwordResetToken) {
        String resetLink = app.getFrontendUrl() + "/reset-password?token=" + passwordResetToken.getToken();
        publisher.publishEvent(new PasswordResetEvent(user.getEmail(), user.getUsername(), resetLink));
    }

    private void publishPasswordChangedMessage(User user) {
        publisher.publishEvent(new PasswordChangedEvent(user.getEmail(), user.getUsername()));
    }
}
