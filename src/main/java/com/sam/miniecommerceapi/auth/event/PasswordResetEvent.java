package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.notification.dto.PasswordResetMailData;

public record PasswordResetEvent(PasswordResetMailData data) {
}
