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
    @Size(min = 2, max = 64, message = "CATEGORY_NAME_SIZE")
    String name;

    @Size(min = 2, max = 64, message = "CATEGORY_SLUG_SIZE")
    String slug;

    @URL(message = "CATEGORY_IMAGE_URL_MUST_BE_URL")
    String imageUrl;
}
