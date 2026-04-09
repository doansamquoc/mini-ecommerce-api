package com.sam.miniecommerceapi.event;

import com.sam.miniecommerceapi.common.constant.MailTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PasswordResetEvent extends EmailEvent {
	public PasswordResetEvent(String to, String username, String resetLink) {
		super(
			to,
			MailTemplate.PASSWORD_RESET.getSubject(),
			Map.of(
				"username", username,
				"resetLink", resetLink,
				"time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
			)
		);
	}
}
