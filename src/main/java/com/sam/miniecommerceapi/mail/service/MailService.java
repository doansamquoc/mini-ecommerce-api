package com.sam.miniecommerceapi.mail.service;

import com.sam.miniecommerceapi.common.enums.MailTemplate;
import com.sam.miniecommerceapi.mail.dto.request.SendMailRequest;

public interface MailService {
    void sendHtmlMail(SendMailRequest r, MailTemplate template, Object data);
}
