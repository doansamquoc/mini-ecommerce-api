package com.sam.miniecommerceapi.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    @NotBlank(message = "user.validation.token_required")
    String token;
    @NotBlank(message = "user.validation.new_password_required")
    @Size(min = 6, message = "user.validation.password_length")
    String newPassword;
}
