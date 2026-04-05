package com.sam.miniecommerceapi.event;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;

import java.util.Map;

public class PasswordChangedEvent extends EmailEvent {
    public PasswordChangedEvent(String to, String username) {
        super(to, MailTemplate.PASSWORD_CHANGED.getSubject(), Map.of("username", username));
    }
}