package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.CreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;

public interface AuthService {
    LoginResult login(LoginRequest request, String ip, String agent);

    UserResponse create(CreationRequest r);
}
