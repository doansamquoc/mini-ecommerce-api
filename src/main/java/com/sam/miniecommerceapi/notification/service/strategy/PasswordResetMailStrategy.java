package com.sam.miniecommerceapi.notification.service.strategy;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import com.sam.miniecommerceapi.notification.dto.PasswordResetMailData;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Component
public class PasswordResetMailStrategy implements MailStrategy {

    @Override
    public MailTemplate getSupportedTemplate() {
        return MailTemplate.PASSWORD_RESET;
    }

    @Override
    public void setContextVariables(Context context, Object data) {
        if (data instanceof PasswordResetMailData d) {
            context.setVariable("email", d.email());
            context.setVariable("username", d.username());
            context.setVariable("ip", d.ip());
            context.setVariable("agent", d.agent());
            context.setVariable("resetLink", d.resetLink());
            context.setVariable("time", LocalDateTime.now(d.clock()));
        }
    }
}
