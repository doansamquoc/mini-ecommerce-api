package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.service.IdentityService;
import com.sam.miniecommerceapi.event.RegisterUserEvent;
import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.mapper.UserMapper;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityServiceImpl implements IdentityService {
    UserMapper userMapper;
    UserService userService;
    ApplicationEventPublisher publisher;

    @Override
    public UserResponse registerUser(UserCreationRequest request) {
        // Create user
        User user = userService.createUser(request);

        // Send an email welcome to user
        publishWelcomeMessage(user);

        return userMapper.toResponse(user);
    }

    private void publishWelcomeMessage(User user) {
        publisher.publishEvent(new RegisterUserEvent(user.getEmail(), user.getUsername()));
    }
}
