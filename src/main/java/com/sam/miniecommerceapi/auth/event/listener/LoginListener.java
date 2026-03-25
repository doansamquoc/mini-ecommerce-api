package com.sam.miniecommerceapi.auth.event.listener;

import com.sam.miniecommerceapi.auth.event.LoginEvent;
import com.sam.miniecommerceapi.common.enums.MailTemplate;
import com.sam.miniecommerceapi.mail.dto.LoginMailData;
import com.sam.miniecommerceapi.mail.dto.request.SendMailRequest;
import com.sam.miniecommerceapi.mail.service.MailService;
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
