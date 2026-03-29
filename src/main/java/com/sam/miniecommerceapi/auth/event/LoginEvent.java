package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.notification.dto.LoginMailData;

public record LoginEvent(LoginMailData data) {
}
