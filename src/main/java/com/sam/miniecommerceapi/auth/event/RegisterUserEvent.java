package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.WelcomeMailData;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

public record RegisterUserEvent(WelcomeMailData data) {
}
