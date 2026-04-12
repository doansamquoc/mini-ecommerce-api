package com.sam.miniecommerceapi.product.dto.request;

import com.sam.miniecommerceapi.common.validator.ExitsId;
import com.sam.miniecommerceapi.upload.entity.Image;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
	@Size(min = 2, max = 64, message = "category.name.size")
	String name;

	@Size(min = 2, max = 64, message = "category.slug.size")
	String slug;

	@ExitsId(entity = Image.class, message = "image.not_found")
	Long imageId;
}