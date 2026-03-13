package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.PasswordChangedMailData;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

public record PasswordChangedEvent(PasswordChangedMailData data) {
}