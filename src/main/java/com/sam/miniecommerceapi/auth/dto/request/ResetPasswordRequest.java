package com.sam.miniecommerceapi.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    @NotBlank(message = "RESET_TOKEN_CANNOT_BLANK")
    String token;
    @NotBlank(message = "RESET_NEW_PASSWORD_CANNOT_BLANK")
    String newPassword;
}
