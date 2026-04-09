package com.sam.miniecommerceapi.config;

import com.sam.miniecommerceapi.auth.config.CookieBearerTokenResolver;
import com.sam.miniecommerceapi.auth.config.OAuth2FailureHandler;
import com.sam.miniecommerceapi.auth.config.OAuth2SuccessHandler;
import com.sam.miniecommerceapi.auth.config.jwt.JwtAccessDeniedHandler;
import com.sam.miniecommerceapi.auth.config.jwt.JwtAuthenticationEntryPoint;
import com.sam.miniecommerceapi.auth.repository.OAuth2AuthorizationRequestRepository;
import com.sam.miniecommerceapi.auth.service.CustomOAuth2UserService;
import com.sam.miniecommerceapi.common.constant.AppConstant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
	JwtDecoder jwtDecoder;
	JwtConverter jwtConverter;
	JwtAccessDeniedHandler accessDeniedHandler;
	CustomOAuth2UserService oAuth2UserService;
	OAuth2SuccessHandler oAuth2SuccessHandler;
	OAuth2FailureHandler oAuth2FailureHandler;
	CookieBearerTokenResolver cookieBearerTokenResolver;
	JwtAuthenticationEntryPoint authenticationEntryPoint;
	OAuth2AuthorizationRequestRepository requestRepository;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors(Customizer.withDefaults());
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity.sessionManagement(
			session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.authorizeHttpRequests(
			auth -> {
				auth.requestMatchers(AppConstant.PUBLIC_ENDPOINTS).permitAll();
				auth.requestMatchers(AppConstant.SWAGGER_ENDPOINTS).permitAll();
				auth.requestMatchers(AppConstant.ADMIN_ENDPOINTS).hasRole("ADMIN");
				auth.anyRequest().authenticated();
			});
		httpSecurity.oauth2Login(oauth2 -> {
			oauth2.authorizationEndpoint(endpoint -> endpoint.authorizationRequestRepository(requestRepository));
			oauth2.userInfoEndpoint(u -> u.userService(oAuth2UserService));
			oauth2.successHandler(oAuth2SuccessHandler);
			oauth2.failureHandler(oAuth2FailureHandler);
		});
		httpSecurity.oauth2ResourceServer(
			oauth2 -> {
				oauth2.bearerTokenResolver(cookieBearerTokenResolver);
				oauth2.authenticationEntryPoint(authenticationEntryPoint);
				oauth2.accessDeniedHandler(accessDeniedHandler);
				oauth2.jwt(
					jwt -> {
						jwt.decoder(jwtDecoder);
						jwt.jwtAuthenticationConverter(jwtConverter);
					});
			});
		return httpSecurity.build();
	}
}
