package com.sam.miniecommerceapi.event;

import com.sam.miniecommerceapi.common.constant.MailTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LoginEvent extends EmailEvent {
    public LoginEvent(String to, String username, String resetLink, String ip, String agent) {
        super(
                to,
                MailTemplate.LOGIN_ALERT.getSubject(),
                Map.of(
                        "email", to,
                        "username", username,
                        "resetLink", resetLink,
                        "ip", ip,
                        "agent", agent,
                        "time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                )
        );
    }
}
