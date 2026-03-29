package com.sam.miniecommerceapi.auth.event.listener;

import com.sam.miniecommerceapi.auth.event.RegisterUserEvent;
import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import com.sam.miniecommerceapi.notification.dto.WelcomeMailData;
import com.sam.miniecommerceapi.notification.dto.request.SendMailRequest;
import com.sam.miniecommerceapi.notification.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterUserListener {
    MailService mailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegisterUserEvent(RegisterUserEvent event) {
        WelcomeMailData data = event.data();
        mailService.sendHtmlMail(new SendMailRequest(data.email()), MailTemplate.WELCOME, data);
    }
}
