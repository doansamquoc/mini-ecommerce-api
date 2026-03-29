package com.sam.miniecommerceapi.notification.service.strategy;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import com.sam.miniecommerceapi.notification.dto.LoginMailData;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Component
public class LoginAlertMailStrategy implements MailStrategy {
    @Override
    public MailTemplate getSupportedTemplate() {
        return MailTemplate.LOGIN_ALERT;
    }

    @Override
    public void setContextVariables(Context context, Object data) {
        if (data instanceof LoginMailData d) {
            context.setVariable("email", d.email());
            context.setVariable("ip", d.ip());
            context.setVariable("agent", d.agent());
            context.setVariable("username", d.username());
            context.setVariable("resetLink", d.resetLink());
            context.setVariable("time", LocalDateTime.now(d.clock()));
        }

    }
}
