package com.sam.miniecommerceapi.notification.service.strategy;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import com.sam.miniecommerceapi.notification.dto.PasswordChangedMailData;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class PasswordChangedMailStrategy implements MailStrategy {
    @Override
    public MailTemplate getSupportedTemplate() {
        return MailTemplate.PASSWORD_CHANGED;
    }

    @Override
    public void setContextVariables(Context context, Object data) {
        if (data instanceof PasswordChangedMailData d) {
            context.setVariable("email", d.email());
            context.setVariable("username", d.username());
        }
    }
}
