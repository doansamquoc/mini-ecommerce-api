package com.sam.miniecommerceapi.upload.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ImageAdditionRequest(
	@NotBlank(message = "image.public_Id.required")
	String publicId,

	@NotBlank(message = "image.url.required")
	@URL(message = "image.url.invalid_url")
	String url
) {}
