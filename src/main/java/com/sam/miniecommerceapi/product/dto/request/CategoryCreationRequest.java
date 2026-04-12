package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {
	@NotBlank(message = "category.name.required")
	@Size(min = 2, max = 64, message = "category.name.size")
	String name;

	@NotBlank(message = "category.slug.required")
	@Size(min = 2, max = 64, message = "category.slug.size")
	String slug;

	@NotNull(message = "category.image_id.required")
	@ExitsId(entity = Image.class, message = "image.not_found")
	Long imageId;
}
