package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.entity.PasswordResetToken;
import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.event.LoginEvent;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.auth.security.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.service.AuthenticationService;
import com.sam.miniecommerceapi.auth.service.PasswordResetTokenService;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    Clock clock;
    @Mock
    UserService userService;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    AppProperties appProperties;
    @Mock
    AuthenticationManager manager;
    @InjectMocks
    AuthenticationServiceImpl authenticationService;
    @Mock
    ApplicationEventPublisher publisher;
    @Mock
    RefreshTokenService refreshTokenService;
    @Mock
    PasswordEncoder encoder;
    @Mock
    PasswordResetTokenService passwordResetTokenService;

    String identifier = "testuser";
    String password = "password123";
    String ip = "127.0.0.1";
    String agent = "Mozilla/5.0";

    @Test
    @DisplayName("Login successfully - Return tokens")
    void login_Success() {
        LoginRequest request = new LoginRequest(identifier, password);

        UserPrincipal mockPrincipal = mock(UserPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(mockPrincipal);

        when(manager.authenticate(any())).thenReturn(mockAuth);

        User mockUser = new User();
        mockUser.setEmail("test@gmail.com");
        when(userService.getReference(1L)).thenReturn(mockUser);

        when(jwtProvider.generateAccessToken(mockPrincipal)).thenReturn("access-token-123");

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setToken("refresh-token-456");
        when(refreshTokenService.createToken(any(User.class), anyString(), anyString())).thenReturn(mockRefreshToken);

        PasswordResetToken mockResetToken = new PasswordResetToken();
        mockResetToken.setToken("reset-token-123");
        when(passwordResetTokenService.createToken(any())).thenReturn(mockResetToken);

        when(appProperties.getFrontendUrl()).thenReturn("http://localhost:3000");

        TokenDTO result = authenticationService.login(request, ip, agent);

        assertThat(result.getAccessToken()).isEqualTo("access-token-123");
        assertThat(result.getRefreshToken()).isEqualTo("refresh-token-456");

        verify(publisher, times(1)).publishEvent(any(LoginEvent.class));
    }

    @Test
    void login() {
    }
}