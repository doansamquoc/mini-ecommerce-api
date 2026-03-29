package com.sam.miniecommerceapi.shared.resolver;

import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.shared.annotation.CurrentUserId;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class) && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public @Nullable Object resolveArgument(
            @NonNull MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }
        throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
    }
}
