package com.sam.miniecommerceapi.user.dto.request;

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
public class UserCreationRequest {
	@NotBlank(message = "user.email.required")
	@Email(message = "user.email.invalid")
	String email;

	@NotBlank(message = "user.password.required")
	@Size(min = 6, message = "user.password.size")
	String password;
}
