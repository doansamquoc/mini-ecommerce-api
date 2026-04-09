package com.sam.miniecommerceapi.auth.config;

import com.sam.miniecommerceapi.auth.repository.OAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
	OAuth2AuthorizationRequestRepository authorizationRequestRepository;

	@Override
	public void onAuthenticationFailure(
		HttpServletRequest req,
		HttpServletResponse res,
		AuthenticationException exception
	) throws IOException {
		String targetUrl = Objects.requireNonNull(WebUtils.getCookie(req, "redirect_uri")).getValue();
		targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("error", exception.getLocalizedMessage())
			.build()
			.toUriString();

		authorizationRequestRepository.removeAuthorizationRequestCookies(res);
		getRedirectStrategy().sendRedirect(req, res, targetUrl);
	}
}
