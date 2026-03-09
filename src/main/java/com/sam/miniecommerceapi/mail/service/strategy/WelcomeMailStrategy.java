package com.sam.miniecommerceapi.mail.service.strategy;

import com.sam.miniecommerceapi.common.enums.MailTemplate;
import com.sam.miniecommerceapi.mail.dto.WelcomeMailData;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class WelcomeMailStrategy implements MailStrategy {

    @Override
    public MailTemplate getSupportedTemplate() {
        return MailTemplate.WELCOME;
    }

    @Override
    public void setContextVariables(Context context, Object data) {
        if (data instanceof WelcomeMailData(String email, String username)) {
            context.setVariable("email", email);
            context.setVariable("username", username);
        }
    }
}
