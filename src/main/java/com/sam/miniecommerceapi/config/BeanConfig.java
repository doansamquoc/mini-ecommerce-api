package com.sam.miniecommerceapi.config;

import com.cloudinary.Cloudinary;
import com.github.slugify.Slugify;
import com.sam.miniecommerceapi.auth.config.jwt.JwtBlacklistValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BeanConfig {
	AppProperties appProperties;

	@Bean
	SecretKey secretKey() {
		byte[] key = appProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
		if (key.length < 32) throw new IllegalArgumentException("JWT secret key must least 256 bit");
		return new SecretKeySpec(key, MacAlgorithm.HS512.getName());
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cors = new CorsConfiguration().applyPermitDefaultValues();
		cors.setAllowedOrigins(List.of("http://localhost:*"));
		cors.setAllowedHeaders(List.of("*"));
		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		cors.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cors);
		return source;
	}

	@Bean
	JwtDecoder jwtDecoder(JwtBlacklistValidator validator) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey()).macAlgorithm(MacAlgorithm.HS512).build();
		OAuth2TokenValidator<Jwt> defaultValidator = new JwtTimestampValidator();
		OAuth2TokenValidator<Jwt> delegatingValidator = new DelegatingOAuth2TokenValidator<>(defaultValidator, validator);
		jwtDecoder.setJwtValidator(delegatingValidator);
		return jwtDecoder;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	@Bean
	Slugify slugify() {
		return Slugify.builder().lowerCase(true).transliterator(true).customReplacement("đ", "d").build();
	}

	@Bean
	Cloudinary cloudinary() {
		Map<String, Object> configs = new HashMap<>();
		configs.put("cloud_name", appProperties.getCloudinaryName());
		configs.put("api_key", appProperties.getCloudinaryApiKey());
		configs.put("api_secret", appProperties.getCloudinaryApiSecret());
		configs.put("secure", true);
		return new Cloudinary(configs);
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/static/**"));
	}
}
