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
    @NotBlank(message = "CATEGORY_NAME_CANNOT_BE_BLANK")
    @Size(min = 2, max = 64, message = "CATEGORY_NAME_SIZE")
    String name;

    @NotBlank(message = "CATEGORY_SLUG_CANNOT_BE_BLANK")
    @Size(min = 2, max = 64, message = "CATEGORY_SLUG_SIZE")
    String slug;

    @NotBlank(message = "CATEGORY_IMAGE_URL_CANNOT_BE_BLANK")
    @URL(message = "CATEGORY_IMAGE_URL_MUST_BE_URL")
    String imageUrl;
}
