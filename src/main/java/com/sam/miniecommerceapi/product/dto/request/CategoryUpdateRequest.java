package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
	@Size(min = 2, max = 64, message = "category.validation.name_size")
	String name;

	@Size(min = 2, max = 64, message = "category.validation.slug_size")
	String slug;

	@URL(message = "category.validation.image_invalid_url")
	String imageUrl;
}
