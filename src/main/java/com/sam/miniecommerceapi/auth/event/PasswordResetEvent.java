package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.PasswordResetMailData;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

public record PasswordResetEvent(PasswordResetMailData data) {
}
