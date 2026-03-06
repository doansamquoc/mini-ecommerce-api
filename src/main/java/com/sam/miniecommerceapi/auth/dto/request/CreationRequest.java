package com.sam.miniecommerceapi.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationRequest {
    @Email(message = "EMAIL_INVALID")
    String email;
    @Size(min = 6, message = "PASSWORD_SIZE")
    String password;
}
