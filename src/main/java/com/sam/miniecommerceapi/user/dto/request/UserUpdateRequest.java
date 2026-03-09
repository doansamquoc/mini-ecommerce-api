package com.sam.miniecommerceapi.user.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String username;
    String email;
    String phone;
    String password;
    String displayName;
}
