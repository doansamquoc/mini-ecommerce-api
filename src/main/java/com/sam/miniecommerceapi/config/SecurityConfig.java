package com.sam.miniecommerceapi.config;

import com.sam.miniecommerceapi.common.constant.AppConstant;
import com.sam.miniecommerceapi.security.exception.JwtAccessDeniedHandler;
import com.sam.miniecommerceapi.security.exception.JwtAuthenticationEntryPoint;
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
  JwtAccessDeniedHandler accessDeniedHandler;
  JwtAuthenticationEntryPoint authenticationEntryPoint;
  JwtConverter jwtConverter;
  JwtDecoder jwtDecoder;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
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
    httpSecurity.oauth2ResourceServer(
        oauth2 -> {
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
