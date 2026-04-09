package com.sam.miniecommerceapi.auth.repository;

import com.cloudinary.utils.StringUtils;
import com.sam.miniecommerceapi.common.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	public static final String AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	private static final int cookieExpireSeconds = 180;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, AUTH_REQUEST_COOKIE_NAME);
		if (cookie == null) return null;
		return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authReq, HttpServletRequest req, HttpServletResponse res) {
		if (authReq == null) {
			CookieUtils.deleteCookie(res, AUTH_REQUEST_COOKIE_NAME, "/");
			CookieUtils.deleteCookie(res, REDIRECT_URI_PARAM_COOKIE_NAME, "/");
		}

		CookieUtils.addCookie(res, AUTH_REQUEST_COOKIE_NAME, CookieUtils.serialize(authReq), cookieExpireSeconds, "/");
		String redirectUriAfterLogin = req.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
			CookieUtils.addCookie(res, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds, "/");
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest req, HttpServletResponse res) {
		OAuth2AuthorizationRequest authRequest = this.loadAuthorizationRequest(req);
		this.removeAuthorizationRequestCookies(res);
		return authRequest;
	}

	public void removeAuthorizationRequestCookies(HttpServletResponse res) {
		CookieUtils.deleteCookie(res, AUTH_REQUEST_COOKIE_NAME, "/");
		CookieUtils.deleteCookie(res, REDIRECT_URI_PARAM_COOKIE_NAME, "/");
	}
}
