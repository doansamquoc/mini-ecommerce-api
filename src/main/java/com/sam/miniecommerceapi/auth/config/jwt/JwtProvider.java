package com.sam.miniecommerceapi.auth.config.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.common.constant.AppConstant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtProvider {
	SecretKey secretKey;
	AppProperties props;

	public String createToken(UserPrincipal user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		Date issueTime = new Date();
		Date expirationTime = new Date(System.currentTimeMillis() + props.getAccessTokenExpiration());
		Set<String> roles = getRoles(user.getAuthorities());

		JWTClaimsSet claims = new JWTClaimsSet.Builder()
			.issuer("Mini E-commerce API")
			.subject(String.valueOf(user.getId()))
			.issueTime(issueTime)
			.expirationTime(expirationTime)
			.jwtID(UUID.randomUUID().toString())
			.claim(AppConstant.AUTHORIZE_CLAIM_NAME, roles)
			.claim("username", user.getUsername())
			.build();

		Payload payload = new Payload(claims.toJSONObject());
		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			jwsObject.sign(new MACSigner(secretKey));
		} catch (JOSEException e) {
			log.error("Generate access token failed {}", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

		return jwsObject.serialize();
	}

	private Set<String> getRoles(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.filter(Objects::nonNull)
			.map(a -> a.replace(AppConstant.AUTHORIZE_PREFIX, ""))
			.collect(Collectors.toSet());
	}
}
