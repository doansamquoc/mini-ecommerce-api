package com.sam.miniecommerceapi.mail.service.strategy;

import com.sam.miniecommerceapi.common.enums.MailTemplate;
import com.sam.miniecommerceapi.mail.dto.PasswordResetMailData;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.time.Clock;
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
            context.setVariable("token", d.token());
            context.setVariable("resetLink", d.resetLink());
            context.setVariable("time", LocalDateTime.now(d.clock()));
        }
    }
}
