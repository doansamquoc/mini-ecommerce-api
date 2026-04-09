package com.sam.miniecommerceapi.listener;

import com.sam.miniecommerceapi.common.constant.MailTemplate;
import com.sam.miniecommerceapi.event.LoginEvent;
import com.sam.miniecommerceapi.event.PasswordChangedEvent;
import com.sam.miniecommerceapi.event.PasswordResetEvent;
import com.sam.miniecommerceapi.event.RegisterUserEvent;
import com.sam.miniecommerceapi.notification.dto.request.TemplateEmailRequest;
import com.sam.miniecommerceapi.notification.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailEventListener {
	EmailService emailService;

	@Async
	@EventListener
	public void handleEvent(LoginEvent event) {
		log.info("Sent login alert email to: {}", event.getTo());
		emailService.sendTemplateEmail(
			new TemplateEmailRequest(
				event.getTo(),
				event.getSubject(),
				MailTemplate.LOGIN_ALERT.getTemplate(),
				event.getVariables()
			)
		);
	}

	@Async
	@EventListener
	public void handleEvent(PasswordChangedEvent event) {
		log.info("Sent password changed email to: {}", event.getTo());
		emailService.sendTemplateEmail(
			new TemplateEmailRequest(
				event.getTo(),
				event.getSubject(),
				MailTemplate.PASSWORD_CHANGED.getTemplate(),
				event.getVariables()
			)
		);
	}

	@Async
	@EventListener
	public void handleEvent(PasswordResetEvent event) {
		log.info("Sent password reset email to: {}", event.getTo());
		emailService.sendTemplateEmail(
			new TemplateEmailRequest(
				event.getTo(),
				event.getSubject(),
				MailTemplate.PASSWORD_RESET.getTemplate(),
				event.getVariables()
			)
		);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleEvent(RegisterUserEvent event) {
		log.info("Sent welcome email to: {}", event.getTo());
		emailService.sendTemplateEmail(
			new TemplateEmailRequest(
				event.getTo(),
				event.getSubject(),
				MailTemplate.WELCOME.getTemplate(),
				event.getVariables()
			)
		);
	}
}
