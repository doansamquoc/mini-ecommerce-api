package com.sam.miniecommerceapi.auth.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sam.miniecommerceapi.common.dto.UserPrincipal;
import com.sam.miniecommerceapi.config.AppProperties;
import com.sam.miniecommerceapi.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Date;
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

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("Mini E-commerce API")
                .subject(user.getId())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", user.getAuthorities())
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

    public boolean validateAccessToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(new Date())) return false;
        return signedJWT.verify(new MACVerifier(secretKey));
    }
}
