package com.sam.miniecommerceapi.common.constant;

public class AppConstant {
  public static final String ALGORITHM = "HmacSHA256";
  public static final String[] PUBLIC_ENDPOINTS = {"/api/v1/auth/**"};
  public static final String[] ADMIN_ENDPOINTS = {"/api/v1/admin/**"};
  public static final String[] SWAGGER_ENDPOINTS = {
    "/v3/api-docs/**", "swagger-ui/**", "swagger-ui.html"
  };
}
