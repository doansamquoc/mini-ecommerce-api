package com.sam.miniecommerceapi.common.audit;

import com.sam.miniecommerceapi.common.annotation.ClientIp;
import com.sam.miniecommerceapi.common.annotation.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuditMetaDataResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ClientIp.class) || parameter.hasParameterAnnotation(UserAgent.class);
    }

    @Override
    public @Nullable Object resolveArgument(
            @NonNull MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) return null;
        if (parameter.hasParameterAnnotation(ClientIp.class)) return servletRequest.getRemoteAddr();
        if (parameter.hasParameterAnnotation(UserAgent.class)) return servletRequest.getHeader("User-Agent");
        return null;
    }
}
