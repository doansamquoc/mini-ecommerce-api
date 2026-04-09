package com.sam.miniecommerceapi.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
	@Size(min = 2, message = "USER_USERNAME_TOO_SHORT")
	@Size(max = 16, message = "USER_USERNAME_TOO_LONG")
	String username;

	@Email(message = "REQUEST_EMAIL_INVALID")
	String email;

	String phone;

	@Size(min = 6, message = "USER_PASSWORD_TOO_SHORT")
	@Size(max = 56, message = "USER_PASSWORD_TOO_LONG")
	String password;

	@Size(min = 2, message = "USER_DISPLAY_NAME_TOO_SHORT")
	@Size(max = 56, message = "USER_DISPLAY_NAME_TOO_LONG")
	String displayName;
}
