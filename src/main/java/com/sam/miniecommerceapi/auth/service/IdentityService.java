package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;

public interface IdentityService {
    UserResponse createUser(UserCreationRequest r);
}
