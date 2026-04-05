package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.Valid;
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
    @NotBlank(message = "product.validation.name_required")
    @Size(min = 2, max = 225, message = "product.validation.name_size")
    String name;

    @Size(min = 2, message = "product.validation.description_size")
    String description;

    @NotNull(message = "category.validation.id_required")
    Long categoryId;

    @NotBlank(message = "product.validation.image_required")
    @URL(message = "product.validation.image_invalid_url")
    String imageUrl;

    @Valid
    List<VariantRequest> variants;
}
