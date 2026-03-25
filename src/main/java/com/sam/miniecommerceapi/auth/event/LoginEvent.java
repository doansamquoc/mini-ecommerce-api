package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.LoginMailData;

public record LoginEvent(LoginMailData data) {
}
