package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.notification.dto.PasswordChangedMailData;

public record PasswordChangedEvent(PasswordChangedMailData data) {
}