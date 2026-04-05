package com.sam.miniecommerceapi.notification.dto.request;

import java.util.List;

public record EmailRequest(
        String to,
        String subject,
        String body,
        String cc,
        String bcc,
        List<String> attachments,
        boolean html
) {}
