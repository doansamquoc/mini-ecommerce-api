package com.sam.miniecommerceapi.mail.service.strategy;

import com.sam.miniecommerceapi.common.enums.MailTemplate;
import com.sam.miniecommerceapi.mail.dto.LoginMailData;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class LoginAlertMailStrategy implements MailStrategy {
    @Override
    public MailTemplate getSupportedTemplate() {
        return MailTemplate.LOGIN_ALERT;
    }

    @Override
    public void setContextVariables(Context context, Object data) {
        if (data instanceof LoginMailData(
                String email, String username, String ip, String agent, String token, Clock clock
        )) {
            context.setVariable("email", email);
            context.setVariable("ip", ip);
            context.setVariable("agent", agent);
            context.setVariable("username", username);
            context.setVariable("token", token);
            context.setVariable("time", LocalDateTime.now(clock));
        }

    }
}
