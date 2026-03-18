package com.sam.miniecommerceapi.common.util;

import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }

        throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
    }
}
