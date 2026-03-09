package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.CreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.event.LoginEvent;
import com.sam.miniecommerceapi.auth.event.RegisterUserEvent;
import com.sam.miniecommerceapi.auth.security.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.service.AuthService;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.dto.UserPrincipal;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.UsernameUtils;
import com.sam.miniecommerceapi.mail.dto.LoginMailData;
import com.sam.miniecommerceapi.mail.dto.WelcomeMailData;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.mapper.UserMapper;
import com.sam.miniecommerceapi.user.repository.UserRepository;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    Clock clock;
    UserMapper mapper;
    UserService userService;
    UserRepository repository;
    AuthenticationManager authManager;
    PasswordEncoder encoder;
    JwtProvider jwtProvider;
    ApplicationEventPublisher publisher;
    RefreshTokenService refreshTokenService;

    private UserPrincipal authenticate(String identifier, String password) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
            return (UserPrincipal) auth.getPrincipal();
        } catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    @Override
    public LoginResult login(LoginRequest r, String ip, String agent) {
        UserPrincipal userPrincipal = authenticate(r.getIdentifier(), r.getPassword());

        String accessToken = jwtProvider.generateAccessToken(userPrincipal);

        User user = userService.getReference(userPrincipal.getId());
        RefreshToken refreshToken = refreshTokenService.create(user, ip, agent);

        LoginMailData data = new LoginMailData(user.getEmail(), user.getUsername(), ip, agent, "Token is null", clock);
        LoginEvent event = new LoginEvent(this, data);
        publisher.publishEvent(event);

        return LoginResult.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public UserResponse register(CreationRequest r) {
        if (userService.existsByEmail(r.getEmail())) throw new BusinessException(ErrorCode.EMAIL_EXISTED);
        String username = UsernameUtils.generateUsername(r.getEmail());

        User user = mapper.toUser(r);
        user.setUsername(username);
        user.setPassword(encoder.encode(r.getPassword()));
        User userSaved = repository.save(user);

        WelcomeMailData data = new WelcomeMailData(userSaved.getEmail(), userSaved.getUsername());
        RegisterUserEvent event = new RegisterUserEvent(this, data);
        publisher.publishEvent(event);

        return mapper.toResponse(userSaved);
    }
}
