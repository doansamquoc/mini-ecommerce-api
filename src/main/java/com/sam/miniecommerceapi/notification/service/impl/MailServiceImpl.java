package com.sam.miniecommerceapi.notification.service.impl;

import com.sam.miniecommerceapi.shared.constant.MailTemplate;
import com.sam.miniecommerceapi.notification.dto.request.SendMailRequest;
import com.sam.miniecommerceapi.notification.service.MailService;
import com.sam.miniecommerceapi.notification.service.strategy.MailStrategy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService {
    JavaMailSender sender;
    SpringTemplateEngine engine;
    Map<MailTemplate, MailStrategy> strategyMap;

    public MailServiceImpl(JavaMailSender sender, SpringTemplateEngine engine, List<MailStrategy> strategies) {
        this.sender = sender;
        this.engine = engine;
        this.strategyMap = strategies.stream().collect(
                Collectors.toUnmodifiableMap(MailStrategy::getSupportedTemplate, Function.identity())
        );
    }

    @Override
    public void sendHtmlMail(SendMailRequest r, MailTemplate template, Object data) {
        MailStrategy strategy = strategyMap.get(template);
        if (strategy == null) {
            log.error("Template {} not support", template.getTemplate());
            throw new IllegalArgumentException("Template not supported: " + template.getTemplate());
        }

        try {
            String content = generateHtmlContent(r, strategy, data);
            MimeMessage message = createMimeMessage(r.getTo(), strategy.getSupportedTemplate().getSubject(), content);

            sender.send(message);
            log.info("Email [{}] sent to {}", strategy.getSupportedTemplate(), r.getTo());
        } catch (MessagingException e) {
            log.error("Send mail failed: {}", e.getMessage());
        }
    }

    private String generateHtmlContent(SendMailRequest r, MailStrategy strategy, Object data) {
        Context context = new Context();
        context.setVariable("email", r.getTo());

        strategy.setContextVariables(context, data);
        return engine.process(strategy.getSupportedTemplate().getTemplate(), context);
    }

    private MimeMessage createMimeMessage(String to, String subject, String content) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        return message;
    }
}
