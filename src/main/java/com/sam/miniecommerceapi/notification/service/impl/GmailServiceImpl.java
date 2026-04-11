package com.sam.miniecommerceapi.notification.service.impl;

import com.sam.miniecommerceapi.common.util.FileUtils;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.notification.dto.request.BulkEmailRequest;
import com.sam.miniecommerceapi.notification.dto.request.EmailRequest;
import com.sam.miniecommerceapi.notification.dto.request.TemplateEmailRequest;
import com.sam.miniecommerceapi.notification.exception.BulkEmailException;
import com.sam.miniecommerceapi.notification.service.EmailService;
import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GmailServiceImpl implements EmailService {
	JavaMailSender sender;
	TemplateEngine engine;
	AppProperties properties;

	@Override
	@Async("emailExecutor")
	public void sendSimpleEmail(EmailRequest request) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(properties.getEmail().getFrom());
			message.setTo(request.to());
			message.setSubject(request.subject());
			message.setText(request.body());

			if (request.cc() != null) message.setCc(request.cc());
			if (request.bcc() != null) message.setBcc(request.bcc());

			sender.send(message);
			log.info("Simple mail sent to: {}", request.to());
		} catch (MailException e) {
			log.error("Failed to send simple email", e);
		}
	}

	@Override
	@Async("emailExecutor")
	public void sendHtmlEmail(EmailRequest request) {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(message, request);
			helper.setReplyTo(properties.getEmail().getReplyTo());

			sender.send(message);
			log.info("HTML email sent to: {}", request.to());
		} catch (MessagingException e) {
			log.error("Failed to send HTML email", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Async("emailExecutor")
	public void sendTemplateEmail(TemplateEmailRequest request) {
		try {
			TemplateOutput output = new StringOutput();
			engine.render(request.templateName(), request.variables(), output);
			sendHtmlEmail(new EmailRequest(request.to(), request.subject(), output.toString(), null, null, null, true));
		} catch (MailException e) {
			log.error("Failed to send template email", e);
		}
	}

	@Override
	@Async("emailExecutor")
	public void sendWithAttachment(EmailRequest request, byte[] attachment, String attachName) {
		try {
			log.info("Preparing send an email to: {}", request.to());
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(message, request);
			helper.addAttachment(attachName, new ByteArrayResource(attachment), FileUtils.getContentType(attachName));

			sender.send(message);
			log.info("Email with attachment sent to: {}", request.to());
		} catch (MessagingException e) {
			log.error("Failed to send email with attachment", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Async("emailExecutor")
	public void sendBulkEmail(BulkEmailRequest request) {
		List<String> failedRecipients = new ArrayList<>();

		for (String recipient : request.recipients()) {
			try {
				sendTemplateEmail(
					new TemplateEmailRequest(
						recipient,
						request.subject(),
						request.templateName(),
						request.variables()
					)
				);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				failedRecipients.add(recipient);
				log.error("Failed to send email to: {}", recipient, e);
			}
		}

		if (!failedRecipients.isEmpty()) {
			throw new BulkEmailException("Failed to send bulk mail to: " + failedRecipients);
		}
	}

	private MimeMessageHelper createMimeMessageHelper(MimeMessage message, EmailRequest request) throws MessagingException {
		MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
		helper.setFrom(properties.getEmail().getFrom());
		helper.setTo(request.to());
		helper.setSubject(request.subject());
		helper.setText(request.body(), true);
		return helper;
	}
}
