package com.sam.miniecommerceapi.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "INVALID_REQUEST")
    String identifier;
    @NotBlank(message = "INVALID_REQUEST")
    String password;
}
