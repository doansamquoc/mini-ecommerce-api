package com.sam.miniecommerceapi.notification.dto.request;

import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.Map;

public record BulkEmailRequest(
        List<@Email String> recipients,
        String subject,
        String templateName,
        Map<String, Object> variables
) {
}
