package com.sam.miniecommerceapi.auth.service;

public interface TokenBlacklistService {
	void addToBlacklist(String token, long remainingTimeInMs);

	boolean isBlacklisted(String token);
}
