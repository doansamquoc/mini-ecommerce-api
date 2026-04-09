package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;

public interface IdentityService {
	UserResponse registerUser(UserCreationRequest r);
}
