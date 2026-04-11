package com.sam.miniecommerceapi.common.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppProperties {
	@NestedConfigurationProperty
	final Email email = new Email();

	@NestedConfigurationProperty
	final Oauth2 oauth2 = new Oauth2();
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

	@Getter
	@Setter
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Oauth2 {
		List<String> authorizedRedirectUris;
	}

	@Getter
	@Setter
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Email {
		String from;
		String replyTo;
	}
}
