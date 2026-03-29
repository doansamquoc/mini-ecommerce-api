package com.sam.miniecommerceapi.shared.util;

import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import com.sam.miniecommerceapi.shared.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
