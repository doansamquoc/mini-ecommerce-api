package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.service.TokenBlacklistService;
import com.sam.miniecommerceapi.common.constant.AppConstant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public void addToBlacklist(String tokenId, long remainingTimeInMs) {
		redisTemplate.opsForValue().set(
			AppConstant.BLACKLIST_PREFIX + tokenId,
			"revoked",
			remainingTimeInMs,
			TimeUnit.MILLISECONDS
		);
	}

	@Override
	public boolean isBlacklisted(String token) {
		return redisTemplate.hasKey(AppConstant.BLACKLIST_PREFIX + token);
	}
}
