package com.sam.miniecommerceapi.config;

import com.sam.miniecommerceapi.common.resolver.ClientIpArgumentResolver;
import com.sam.miniecommerceapi.common.resolver.CurrentUserIdArgumentResolver;
import com.sam.miniecommerceapi.common.resolver.UserAgentArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new ClientIpArgumentResolver());
		resolvers.add(new UserAgentArgumentResolver());
		resolvers.add(new CurrentUserIdArgumentResolver());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS").allowedHeaders("*");
	}
}
