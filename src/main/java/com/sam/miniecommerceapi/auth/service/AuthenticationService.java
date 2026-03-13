package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import org.springframework.stereotype.Service;

public interface AuthenticationService {

    LoginResult login(LoginRequest r, String ip, String agent);
}
