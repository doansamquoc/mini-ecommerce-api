package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.notification.dto.WelcomeMailData;

public record RegisterUserEvent(WelcomeMailData data) {
}
