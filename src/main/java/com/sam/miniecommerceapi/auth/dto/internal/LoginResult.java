package com.sam.miniecommerceapi.auth.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResult {
    String accessToken;
    String refreshToken;
}
