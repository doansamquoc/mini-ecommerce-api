package com.sam.miniecommerceapi.common.config;

import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    JwtGrantedAuthoritiesConverter authoritiesConverter;

    public JwtConverter() {
        this.authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        this.authoritiesConverter.setAuthoritiesClaimName("roles");
        this.authoritiesConverter.setAuthorityPrefix("ROLE_");
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.authoritiesConverter.convert(jwt);

        Long id = Long.valueOf(jwt.getSubject());
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("username");

        UserPrincipal principal = UserPrincipal.builder()
                        .id(id)
                        .email(email)
                        .username(username)
                        .authorities(authorities)
                        .build();

        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }
}
