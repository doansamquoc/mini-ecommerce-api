package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.event.LoginEvent;
import com.sam.miniecommerceapi.auth.config.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.service.AuthenticationService;
import com.sam.miniecommerceapi.auth.service.PasswordResetTokenService;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.mail.dto.LoginMailData;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    Clock clock;
    UserService userService;
    JwtProvider jwtProvider;
    AppProperties appProperties;
    AuthenticationManager manager;
    ApplicationEventPublisher publisher;
    RefreshTokenService refreshTokenService;
    PasswordResetTokenService passwordResetTokenService;

    UserPrincipal authenticate(String identifier, String password) {
        try {
            Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
            return (UserPrincipal) auth.getPrincipal();
        } catch (BadCredentialsException bce) {
            log.warn("Wrong password [{}].", identifier);
            throw new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        } catch (LockedException le) {
            log.warn("Account [{}] is locked.", identifier);
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        } catch (DisabledException de) {
            log.warn("Account [{}] not activated.", identifier);
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        } catch (AccountExpiredException aee) {
            log.warn("Account [{}] has expired.", identifier);
            throw new BusinessException(ErrorCode.ACCOUNT_EXPIRED);
        } catch (AuthenticationException ae) {
            log.error("Authenticate unknown error [{}].", ae.getMessage());
            throw new BusinessException(ErrorCode.AUTH_FAILED);
        }
    }

    @Override
    public TokenDTO login(LoginRequest r, String ip, String agent) {
        UserPrincipal userPrincipal = authenticate(r.getIdentifier(), r.getPassword());
        User user = userService.findById(userPrincipal.getId());

        String accessToken = jwtProvider.generateAccessToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createToken(userPrincipal.getId(), ip, agent);

        publishLoginAlertMessage(user, ip, agent);
        return new TokenDTO(accessToken, refreshToken.getToken());
    }

    private void publishLoginAlertMessage(User user, String ip, String agent) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.createToken(user);
        String resetLink = appProperties.getFrontendUrl() + "/reset-password?token=" + passwordResetToken.getToken();

        LoginMailData data = new LoginMailData(user.getEmail(), user.getUsername(), ip, agent, resetLink, clock);
        publisher.publishEvent(new LoginEvent(data));
    }
}
