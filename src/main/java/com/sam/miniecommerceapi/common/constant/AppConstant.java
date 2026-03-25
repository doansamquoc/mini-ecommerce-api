package com.sam.miniecommerceapi.common.constant;

public class AppConstant {
    public static final String ALGORITHM = "HmacSHA256";
    public static final String[] PUBLIC_ENDPOINTS = {"/api/v1/auth/**"};
    public static final String[] ADMIN_ENDPOINTS = {"/api/v1/admin/**"};
    public static final String[] SWAGGER_ENDPOINTS = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";

    public static final String PASSWORD_CHANGED_TEMPLATE = "mail/templates/password-changed";
    public static final String PASSWORD_RESET_TEMPLATE = "mail/templates/password-reset";
    public static final String LOGIN_ALERT_TEMPLATE = "mail/templates/login-alert";
    public static final String WELCOME_TEMPLATE = "mail/templates/welcome";

    public static final String LOGGED_IN_USER_OR_ADMIN = "#id == authentication.principal.id or hasRole('ADMIN')";
    public static final String ADMIN = "hasRole('ADMIN')";

    public static final String BLACKLIST_PREFIX = "jwt:blacklist:";
}
