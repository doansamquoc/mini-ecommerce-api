package com.sam.miniecommerceapi.notification.service;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import com.sam.miniecommerceapi.notification.dto.request.SendMailRequest;

public interface MailService {
    void sendHtmlMail(SendMailRequest r, MailTemplate template, Object data);
}
