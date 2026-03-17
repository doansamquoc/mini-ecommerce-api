package com.sam.miniecommerceapi.auth.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sam.miniecommerceapi.common.enums.Role;
import com.sam.miniecommerceapi.user.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPrincipal implements UserDetails {
    Long id;
    String email;
    String username;
    @JsonIgnore
    String password;
    Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        return UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(extractAuthorities(user.getRoles()))
                .build();
    }

    private static Collection<? extends GrantedAuthority> extractAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet());
    }

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public @NullMarked String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
