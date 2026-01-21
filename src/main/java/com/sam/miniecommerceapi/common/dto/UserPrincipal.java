package com.sam.miniecommerceapi.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPrincipal {
  String id;
  String email;
  String username;
  Collection<? extends GrantedAuthority> authorities;
}
