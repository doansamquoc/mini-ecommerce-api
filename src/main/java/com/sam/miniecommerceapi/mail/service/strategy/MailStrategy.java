package com.sam.miniecommerceapi.mail.service.strategy;

import com.sam.miniecommerceapi.common.enums.MailTemplate;
import org.thymeleaf.context.Context;

public interface MailStrategy {
    MailTemplate getSupportedTemplate();
    void setContextVariables(Context context, Object data);
}
