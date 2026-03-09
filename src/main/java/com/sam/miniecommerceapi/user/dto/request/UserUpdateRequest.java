package com.sam.miniecommerceapi.user.dto.request;

import com.sam.miniecommerceapi.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

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
