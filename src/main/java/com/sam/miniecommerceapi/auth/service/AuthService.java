package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;

public interface AuthService {
    LoginResult login(LoginRequest request, String ip, String agent);
}
