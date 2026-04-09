package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;

public interface TokenManagementService {
	TokenDTO refreshToken(String refreshTokenStr);
}
