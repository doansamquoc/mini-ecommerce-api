package com.sam.miniecommerceapi.common.util;

import com.sam.miniecommerceapi.common.constant.AppConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtils {
	public static void addCookie(HttpServletResponse res, String name, String value, long maxAge, String path) {
		String cookie = ResponseCookie.from(name, value)
			.maxAge(maxAge)
			.path(path)
			.sameSite("lax")
			.secure(true)
			.httpOnly(true)
			.build()
			.toString();
		res.addHeader(HttpHeaders.SET_COOKIE, cookie);
	}

	public static void addRefreshToken(HttpServletResponse res, String value, long maxAge) {
		addCookie(res, AppConstant.REFRESH_TOKEN_COOKIE_NAME, value, maxAge, AppConstant.REFRESH_TOKEN_COOKIE_PATH);
	}

	public static void addAccessToken(HttpServletResponse res, String value, long maxAge) {
		addCookie(res, AppConstant.ACCESS_TOKEN_COOKIE_NAME, value, maxAge, AppConstant.ACCESS_TOKEN_COOKIE_PATH);
	}

	public static void deleteCookie(HttpServletResponse res, String name, String path) {
		addCookie(res, name, "", 0, path);
	}

	public static void deleteRefreshToken(HttpServletResponse res) {
		deleteCookie(res, AppConstant.REFRESH_TOKEN_COOKIE_NAME, AppConstant.REFRESH_TOKEN_COOKIE_PATH);
	}

	public static void deleteAccessToken(HttpServletResponse res) {
		deleteCookie(res, AppConstant.ACCESS_TOKEN_COOKIE_NAME, AppConstant.ACCESS_TOKEN_COOKIE_PATH);
	}

	public static String serialize(Object object) {
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(SerializationUtils.clone(Base64.getUrlDecoder().decode(cookie.getValue())));
	}
}
