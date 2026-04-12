package com.sam.miniecommerceapi.common.constant;

public class AppConstant {

	public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
	public static final String REFRESH_TOKEN_COOKIE_PATH = "/api/v1/auth";

	public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
	public static final String ACCESS_TOKEN_COOKIE_PATH = "/";

	public static final String PASSWORD_CHANGED_TEMPLATE = "email/templates/password-changed.jte";
	public static final String PASSWORD_RESET_TEMPLATE = "email/templates/password-reset.jte";
	public static final String LOGIN_ALERT_TEMPLATE = "email/templates/login-alert.jte";
	public static final String WELCOME_TEMPLATE = "email/templates/welcome.jte";
	public static final String LOGGED_IN_USER_OR_ADMIN = "#id == authentication.principal.id or hasRole('ADMIN')";
	public static final String ADMIN = "hasRole('ADMIN')";
	public static final String MANAGER_OR_ADMIN = "hasRole('MANAGER') or hasRole('ADMIN')";
	public static final String BLACKLIST_PREFIX = "jwt:blacklist:";
	public static final String AUTHORIZE_PREFIX = "ROLE_";
	public static final String AUTHORIZE_CLAIM_NAME = "roles";
}
