package com.sam.miniecommerceapi.config;

import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.shared.constant.AppConstant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    JwtGrantedAuthoritiesConverter authoritiesConverter;

    public JwtConverter() {
        this.authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        this.authoritiesConverter.setAuthoritiesClaimName(AppConstant.JWT_AUTHORIZE_CLAIM_NAME);
        this.authoritiesConverter.setAuthorityPrefix(AppConstant.JWT_AUTHORIZE_PREFIX);
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.authoritiesConverter.convert(jwt);

        Long id = Long.valueOf(jwt.getSubject());
        String username = jwt.getClaimAsString("username");

        UserPrincipal principal = UserPrincipal.builder()
                .id(id)
                .username(username)
                .authorities(authorities)
                .jwtId(jwt.getId())
                .expiresAt(jwt.getExpiresAt())
                .build();

        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }
}
