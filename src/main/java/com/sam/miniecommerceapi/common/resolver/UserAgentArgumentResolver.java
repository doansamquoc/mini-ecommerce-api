package com.sam.miniecommerceapi.common.resolver;

import com.sam.miniecommerceapi.common.annotation.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserAgentArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UserAgent.class) && parameter.getParameterType().equals(String.class);
	}

	@Override
	public @Nullable Object resolveArgument(
		@NonNull MethodParameter parameter,
		@Nullable ModelAndViewContainer mavContainer,
		@NonNull NativeWebRequest webRequest,
		@Nullable WebDataBinderFactory binderFactory
	) {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		return request == null ? null : request.getHeader("User-Agent");
	}
}
