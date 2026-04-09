package com.sam.miniecommerceapi.auth.dto.internal;

public record TokenDTO(
	String accessToken,
	String refreshToken
) {}
