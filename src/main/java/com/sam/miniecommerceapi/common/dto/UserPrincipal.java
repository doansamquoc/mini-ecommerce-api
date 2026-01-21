package com.sam.miniecommerceapi.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sam.miniecommerceapi.user.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPrincipal {
  String id;
  String email;
  String username;
  Collection<? extends GrantedAuthority> authorities;
}
