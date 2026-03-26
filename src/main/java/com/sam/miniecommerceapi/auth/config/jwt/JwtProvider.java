package com.sam.miniecommerceapi.auth.config.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.common.config.AppProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtProvider {
    SecretKey secretKey;
    AppProperties app;

    public String generateAccessToken(UserPrincipal user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        Date issueTime = new Date();
        Date expirationTime = new Date(System.currentTimeMillis() + app.getAccessTokenExpiration());
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .map(a -> a.replace("ROLE_", ""))
                .toList();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("Mini E-commerce API")
                .subject(String.valueOf(user.getId()))
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .build();


        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            log.error("Generate access token failed, error {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return jwsObject.serialize();
    }
}
