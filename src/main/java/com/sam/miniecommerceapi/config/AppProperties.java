package com.sam.miniecommerceapi.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppProperties {
    String secretKey;
    Long refreshTokenExpiration;
    Long accessTokenExpiration;
    Long passwordResetTokenExpiration;
    boolean isProduction;
    String frontendUrl;

    // Order
    Long cancellationDeadline;

    // api-docs
    String openApiTitle;
    String openApiVersion;
    String openApiProdServer;

    // Cloudinary
    String cloudinaryName;
    String cloudinaryApiKey;
    String cloudinaryApiSecret;
    String cloudinaryUrl;

    @NestedConfigurationProperty
    final Email email = new Email();

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Email {
        String from;
        String replyTo;
    }
}
