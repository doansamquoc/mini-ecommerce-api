package com.sam.miniecommerceapi.auth.config;

import com.sam.miniecommerceapi.common.constant.AppConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.util.WebUtils;

@Configuration
public class CookieBearerTokenResolver implements BearerTokenResolver {
	@Override
	public String resolve(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, AppConstant.ACCESS_TOKEN_COOKIE_NAME);
		if (cookie != null) return cookie.getValue();
		return null;
	}
}
