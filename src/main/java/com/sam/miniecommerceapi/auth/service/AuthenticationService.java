package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;

public interface AuthenticationService {

    TokenDTO login(LoginRequest r, String ip, String agent);
}
