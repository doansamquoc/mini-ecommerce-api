package com.sam.miniecommerceapi.notification.dto.request;

import java.util.Map;

public record TemplateEmailRequest(String to, String subject, String templateName, Map<String, Object> variables) {}
