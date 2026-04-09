package com.sam.miniecommerceapi.auth.config;

import com.sam.miniecommerceapi.auth.config.jwt.JwtProvider;
import com.sam.miniecommerceapi.auth.repository.OAuth2AuthorizationRequestRepository;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.auth.service.RefreshTokenService;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.CookieUtils;
import com.sam.miniecommerceapi.config.AppProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	JwtProvider jwtProvider;
	AppProperties properties;
	RefreshTokenService tokenService;
	OAuth2AuthorizationRequestRepository authorizationRequestRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
		String targetUrl = determineTargetUrl(req, res, auth);
		if (res.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}
		clearAuthenticationAttributes(req, res);
		getRedirectStrategy().sendRedirect(req, res, targetUrl);
	}

	@Override
	protected String determineTargetUrl(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
		Optional<String> redirectUri = Optional.ofNullable(WebUtils.getCookie(req, "redirect_uri")).map(Cookie::getValue);
		if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
			throw BusinessException.of(ErrorCode.REQUEST_INVALID);
		}
		String targetUrl = redirectUri.orElse(properties.getFrontendUrl() + "/oauth2/callback");
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

		long refreshMaxAgeInSeconds = properties.getRefreshTokenExpiration() / 1000;
		long accessMaxAgeInSeconds = properties.getAccessTokenExpiration() / 1000;
		String accessToken = jwtProvider.createToken(userPrincipal);
		String refreshToken = tokenService.createToken(userPrincipal.getId()).getToken();

		CookieUtils.addRefreshToken(res, refreshToken, refreshMaxAgeInSeconds);
		CookieUtils.addAccessToken(res, accessToken, accessMaxAgeInSeconds);

		return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
	}

	private void clearAuthenticationAttributes(HttpServletRequest req, HttpServletResponse res) {
		super.clearAuthenticationAttributes(req);
		authorizationRequestRepository.removeAuthorizationRequest(req, res);
	}

	private boolean isAuthorizedRedirectUri(String uri) {
		return properties.getOauth2().getAuthorizedRedirectUris().stream().anyMatch(authorizedRedirectUri -> {
			URI authURI = URI.create(authorizedRedirectUri);
			URI clientURI = URI.create(uri);
			return authURI.getHost().equalsIgnoreCase(clientURI.getHost()) && authURI.getPort() == clientURI.getPort();
		});
	}
}
