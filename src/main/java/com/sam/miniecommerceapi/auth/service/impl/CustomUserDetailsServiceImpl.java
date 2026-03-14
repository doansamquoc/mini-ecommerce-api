package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.service.CustomUserDetailsService;
import com.sam.miniecommerceapi.auth.security.UserPrincipal;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.service.UserService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    UserService userService;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String identifier) throws UsernameNotFoundException {
        User user = userService.findByIdentifier(identifier);
        return UserPrincipal.create(user);
    }
}
