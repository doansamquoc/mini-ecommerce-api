package com.sam.miniecommerceapi.notification.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public record BulkEmailRequest(
	@NotEmpty(message = "email.validation.recipient_required")
	List<@Email(message = "email.validation.invalid_email") String> recipients,
	@NotBlank(message = "email.validation.subject_required")
	@Size(min = 2, max = 255, message = "email.validation.subject_size")
	String subject,
	String templateName,
	Map<String, Object> variables
) {}
