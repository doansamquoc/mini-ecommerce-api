package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @Size(min=2, max = 255, message = "product.validation.name_size")
    String name;
    @Size(min = 2, message = "product.validation.description_size")
    String description;
    Long categoryId;
    @URL(message = "product.validation.image_invalid_url")
    String imageUrl;
    Set<VariantUpdateRequest> variants;
}
