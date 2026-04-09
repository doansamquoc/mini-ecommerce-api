package com.sam.miniecommerceapi.notification.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EmailRequest(
	@NotBlank(message = "email.validation.send_to_required")
	@Email(message = "email.validation.invalid_email")
	String to,
	@NotBlank(message = "email.validation.subject_required")
	@Size(min = 2, max = 255, message = "email.validation.subject_size")
	String subject,
	@NotBlank(message = "email.validation.body_required")
	String body,
	String cc,
	String bcc,
	List<String> attachments,
	Boolean html
) {}
