package com.sam.miniecommerceapi.notification.service;

import com.sam.miniecommerceapi.notification.dto.request.BulkEmailRequest;
import com.sam.miniecommerceapi.notification.dto.request.EmailRequest;
import com.sam.miniecommerceapi.notification.dto.request.TemplateEmailRequest;

public interface EmailService {
	void sendSimpleEmail(EmailRequest request);

	void sendHtmlEmail(EmailRequest request);

	void sendTemplateEmail(TemplateEmailRequest request);

	void sendWithAttachment(EmailRequest request, byte[] attachment, String attachName);

	void sendBulkEmail(BulkEmailRequest request);
}
