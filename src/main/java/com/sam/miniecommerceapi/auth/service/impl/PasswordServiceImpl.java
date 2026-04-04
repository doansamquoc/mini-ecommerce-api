package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.auth.event.PasswordChangedEvent;
import com.sam.miniecommerceapi.auth.event.PasswordResetEvent;
import com.sam.miniecommerceapi.auth.service.PasswordResetTokenService;
import com.sam.miniecommerceapi.auth.service.PasswordService;
import com.sam.miniecommerceapi.config.AppProperties;
import com.sam.miniecommerceapi.notification.dto.PasswordChangedMailData;
import com.sam.miniecommerceapi.notification.dto.PasswordResetMailData;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordServiceImpl implements PasswordService {
    Clock clock;
    UserService userService;
    AppProperties app;
    PasswordEncoder encoder;
    ApplicationEventPublisher publisher;
    PasswordResetTokenService passwordResetTokenService;

    /**
     * Forgot password. Find an account with email. If account not found then continue without send mail message
     *
     * @param r     ForgotPasswordRequest
     * @param ip    Client IP
     * @param agent User Agent
     */
    @Override
    public void forgotPassword(ForgotPasswordRequest r, String ip, String agent) {
        userService.findOptionalByEmail(r.getEmail()).ifPresent(user -> {
            PasswordResetToken passwordResetToken = passwordResetTokenService.createToken(user);

            passwordResetTokenService.createToken(user);

            publishForgotPasswordMessage(user, ip, agent, passwordResetToken);
        });
    }

    @Override
    public void resetPassword(ResetPasswordRequest r, String ip, String agent) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(r.getToken());
        User user = passwordResetToken.getUser();

        String hashPassword = encoder.encode(r.getNewPassword());

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().password(hashPassword).build();
        userService.updateUser(user.getId(), userUpdateRequest);

        publishPasswordChangedMessage(user);
    }

    @Override
    public PasswordResetToken validateResetToken(String resetTokenStr) {
        return passwordResetTokenService.validateToken(resetTokenStr);
    }

    private void publishForgotPasswordMessage(
            User user,
            String ip,
            String agent,
            PasswordResetToken passwordResetToken
    ) {
        String resetLink = app.getFrontendUrl() + "/reset-password?token=" + passwordResetToken.getToken();

        PasswordResetMailData data = new PasswordResetMailData(
                user.getEmail(), user.getUsername(), ip, agent, resetLink, clock
        );

        publisher.publishEvent(new PasswordResetEvent(data));
    }

    private void publishPasswordChangedMessage(User user) {
        PasswordChangedMailData data = new PasswordChangedMailData(user.getEmail(), user.getPassword());
        publisher.publishEvent(new PasswordChangedEvent(data));
    }
}
