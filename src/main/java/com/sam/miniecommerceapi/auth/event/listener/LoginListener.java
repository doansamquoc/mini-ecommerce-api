package com.sam.miniecommerceapi.auth.event.listener;

import com.sam.miniecommerceapi.auth.event.LoginEvent;
import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import com.sam.miniecommerceapi.notification.dto.LoginMailData;
import com.sam.miniecommerceapi.notification.dto.request.SendMailRequest;
import com.sam.miniecommerceapi.notification.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginListener {
    MailService mailService;

    @Async
    @EventListener
    public void handleLoginEvent(LoginEvent event) {
        LoginMailData data = event.data();
        mailService.sendHtmlMail(new SendMailRequest(data.email()), MailTemplate.LOGIN_ALERT, data);
    }
}
