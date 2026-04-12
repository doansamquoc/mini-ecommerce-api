package com.sam.miniecommerceapi.common.constant;

public class APIs {
	public static final String PREFIX_V1 = "/api/v1";
	public static final String[] PUBLIC_ENDPOINTS = {"/", PREFIX_V1 + "/auth/**", "/actuator/**", "/oauth2/authorization/**", "/login/oauth2/code/**"};
	public static final String[] SWAGGER_ENDPOINTS = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};
	public static final String[] ADMIN_ENDPOINTS = {PREFIX_V1 + "/admin/**"};
}
