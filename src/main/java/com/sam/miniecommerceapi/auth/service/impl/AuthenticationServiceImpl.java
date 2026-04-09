package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.config.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;
import com.sam.miniecommerceapi.auth.dto.request.LoginRequest;
import com.sam.miniecommerceapi.auth.entity.RefreshToken;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.auth.service.AuthenticationService;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
	JwtProvider jwtProvider;
	AuthenticationManager manager;
	RefreshTokenService tokenService;

	UserPrincipal authenticate(String identifier, String password) {
		try {
			Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
			return (UserPrincipal) auth.getPrincipal();
		} catch (BadCredentialsException bce) {
			log.warn("Wrong password [{}].", identifier);
			throw BusinessException.of(ErrorCode.AUTH_INVALID_CREDENTIALS);
		} catch (LockedException le) {
			log.warn("Account [{}] is locked.", identifier);
			throw BusinessException.of(ErrorCode.ACCOUNT_LOCKED);
		} catch (DisabledException de) {
			log.warn("Account [{}] not activated.", identifier);
			throw BusinessException.of(ErrorCode.ACCOUNT_DISABLED);
		} catch (AccountExpiredException aee) {
			log.warn("Account [{}] has expired.", identifier);
			throw BusinessException.of(ErrorCode.ACCOUNT_EXPIRED);
		} catch (AuthenticationException ae) {
			log.error("Authenticate unknown error [{}].", ae.getMessage());
			throw BusinessException.of(ErrorCode.AUTH_FAILED);
		}
	}

	@Override
	public TokenDTO login(LoginRequest request) {
		UserPrincipal userPrincipal = authenticate(request.getIdentifier(), request.getPassword());
		String accessToken = jwtProvider.createToken(userPrincipal);
		RefreshToken refreshToken = tokenService.createToken(userPrincipal.getId());
		return new TokenDTO(accessToken, refreshToken.getToken());
	}
}
