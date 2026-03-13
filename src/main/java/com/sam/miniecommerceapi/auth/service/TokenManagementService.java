package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;

public interface TokenManagementService {
    LoginResult refreshAccessToken(String refreshTokenStr, String ip, String agent);
}
