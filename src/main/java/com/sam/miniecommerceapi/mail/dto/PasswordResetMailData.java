package com.sam.miniecommerceapi.mail.dto;

import java.time.Clock;

public record PasswordResetMailData(
        String email,
        String username,
        String ip,
        String agent,
        String token,
        String resetLink,
        Clock clock
) {
}
