package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {
    @NotBlank(message = "PRODUCT_NAME_CANNOT_BE_BLANK")
    @Size(min = 2, max = 225, message = "PRODUCT_NAME_SIZE")
    String name;

    @Size(min = 2, message = "PRODUCT_DESCRIPTION_SIZE")
    String description;

    @NotNull(message = "CATEGORY_ID_CANNOT_BE_BLANK")
    Long categoryId;

    @NotBlank(message = "PRODUCT_IMAGE_URL_CANNOT_BE_BLANK")
    @URL(message = "PRODUCT_IMAGE_URL_MUST_BE_URL")
    String imageUrl;

    List<VariantRequest> variants;
}
