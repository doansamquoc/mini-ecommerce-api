package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {
	@NotBlank(message = "category.validation.name_required")
	@Size(min = 2, max = 64, message = "category.validation.name_size")
	String name;

	@NotBlank(message = "category.validation.slug_required")
	@Size(min = 2, max = 64, message = "category.validation.slug_size")
	String slug;

	@NotBlank(message = "category.validation.image_required")
	@URL(message = "category.validation.image_invalid_url")
	String imageUrl;
}
