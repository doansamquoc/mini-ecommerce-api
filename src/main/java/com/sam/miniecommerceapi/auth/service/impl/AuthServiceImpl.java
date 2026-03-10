package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.internal.LoginResult;
import com.sam.miniecommerceapi.auth.dto.request.CreationRequest;
import com.sam.miniecommerceapi.auth.dto.request.ForgotPasswordRequest;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.dto.request.ResetPasswordRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.event.LoginEvent;
import com.sam.miniecommerceapi.auth.event.PasswordChangedEvent;
import com.sam.miniecommerceapi.auth.event.PasswordResetEvent;
import com.sam.miniecommerceapi.auth.event.RegisterUserEvent;
import com.sam.miniecommerceapi.auth.security.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.service.AuthService;
import com.sam.miniecommerceapi.auth.service.PasswordResetTokenService;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.common.dto.UserPrincipal;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.enums.Role;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.UsernameUtils;
import com.sam.miniecommerceapi.mail.dto.LoginMailData;
import com.sam.miniecommerceapi.mail.dto.PasswordChangedMailData;
import com.sam.miniecommerceapi.mail.dto.PasswordResetMailData;
import com.sam.miniecommerceapi.mail.dto.WelcomeMailData;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    Clock clock;
    UserMapper mapper;
    UserService userService;
    PasswordEncoder encoder;
    JwtProvider jwtProvider;
    UserRepository repository;
    AppProperties appProperties;
    AuthenticationManager authManager;
    ApplicationEventPublisher publisher;
    RefreshTokenService refreshTokenService;
    PasswordResetTokenService passwordResetTokenService;

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

        User user = userService.getReference(userPrincipal.getId());

        LoginMailData data = new LoginMailData(user.getEmail(), user.getUsername(), ip, agent, "Token is null", clock);
        publisher.publishEvent(new LoginEvent(this, data));

        return buildLoginResult(userPrincipal, ip, agent);
    }

    @Override
    public UserResponse register(CreationRequest r) {
        if (userService.existsByEmail(r.getEmail())) throw new BusinessException(ErrorCode.EMAIL_EXISTED);
        String username = UsernameUtils.generateUsername(r.getEmail());

        User user = mapper.toUser(r);
        user.setUsername(username);
        user.setPassword(encoder.encode(r.getPassword()));
        user.setRoles(Set.of(Role.USER));

        try {
            User userSaved = repository.save(user);
            WelcomeMailData data = new WelcomeMailData(userSaved.getEmail(), userSaved.getUsername());
            publisher.publishEvent(new RegisterUserEvent(this, data));

            return mapper.toResponse(userSaved);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTED);
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest r, String ip, String agent) {
        // If user existed by email then send mail
        userService.findOptinalByEmail(r.getEmail()).ifPresent(u -> {
            String token = passwordResetTokenService.createToken(u);
            String resetLink = appProperties.getFrontendUrl() + "/reset-password?token=" + token;

            PasswordResetMailData data = new PasswordResetMailData(
                    u.getEmail(), u.getUsername(), ip, agent, token, resetLink, clock
            );
            publisher.publishEvent(new PasswordResetEvent(this, data));
        });
        // If not, continue
    }

    @Override
    public void resetPassword(ResetPasswordRequest r) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.validateToken(r.getToken());

        String hashedPassword = encoder.encode(r.getNewPassword());
        User user = passwordResetToken.getUser();

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().password(hashedPassword).build();
        userService.updateUser(user.getId(), userUpdateRequest);

        PasswordChangedMailData data = new PasswordChangedMailData(user.getEmail(), user.getUsername());
        publisher.publishEvent(new PasswordChangedEvent(this, data));
    }

    @Override
    public PasswordResetToken validateToken(String token) {
        return passwordResetTokenService.validateToken(token);
    }

    @Transactional
    @Override
    public LoginResult refresh(String tokenString, String ip, String agent) {
        if (tokenString == null) throw new BusinessException(ErrorCode.TOKEN_INVALID);

        RefreshToken refreshToken = refreshTokenService.validateToken(tokenString);
        User user = refreshToken.getUser();

        refreshTokenService.revoke(refreshToken);

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return buildLoginResult(userPrincipal, ip, agent);
    }

    private LoginResult buildLoginResult(UserPrincipal userPrincipal, String ip, String agent) {
        String accessToken = jwtProvider.generateAccessToken(userPrincipal);

        User user = userService.getReference(userPrincipal.getId());
        RefreshToken refreshToken = refreshTokenService.createToken(user, ip, agent);

        return new LoginResult(accessToken, refreshToken.getToken());
    }
}
