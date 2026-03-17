package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.auth.event.RegisterUserEvent;
import com.sam.miniecommerceapi.auth.service.IdentityService;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.UsernameUtils;
import com.sam.miniecommerceapi.mail.dto.WelcomeMailData;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.mapper.UserMapper;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityServiceImpl implements IdentityService {
    UserMapper userMapper;
    UserService userService;
    PasswordEncoder encoder;
    ApplicationEventPublisher publisher;

    @Override
    public UserResponse createUser(UserCreationRequest r) {
        User user = enrichUser(r);

        try {
            user = userService.saveUser(user);
        } catch (DataIntegrityViolationException dive) {
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }

        publishWelcomeMessage(user);

        return userMapper.toResponse(user);
    }

    private User enrichUser(UserCreationRequest r) {
        User user = userMapper.toUser(r);
        user.setUsername(UsernameUtils.generateUsername(r.getEmail()));
        user.setPassword(encoder.encode(r.getPassword()));
        return user;
    }

    private void publishWelcomeMessage(User user) {
        WelcomeMailData data = new WelcomeMailData(user.getEmail(), user.getUsername());
        publisher.publishEvent(new RegisterUserEvent(data));
    }
}
