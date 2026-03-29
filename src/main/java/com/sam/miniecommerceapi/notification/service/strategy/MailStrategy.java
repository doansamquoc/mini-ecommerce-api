package com.sam.miniecommerceapi.notification.service.strategy;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import org.thymeleaf.context.Context;

public interface MailStrategy {
    MailTemplate getSupportedTemplate();
    void setContextVariables(Context context, Object data);
}
