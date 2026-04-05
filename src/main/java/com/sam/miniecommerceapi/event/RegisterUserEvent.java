package com.sam.miniecommerceapi.event;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;

import java.util.Map;

public class RegisterUserEvent extends EmailEvent {
    public RegisterUserEvent(String to, String username) {
        super(to, MailTemplate.WELCOME.getSubject(), Map.of("username", username));
    }
}
