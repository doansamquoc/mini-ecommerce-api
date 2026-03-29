package com.sam.miniecommerceapi.notification.dto;

import java.time.Clock;

public record PasswordResetMailData(
        String email,
        String username,
        String ip,
        String agent,
        String resetLink,
        Clock clock
) {
}
