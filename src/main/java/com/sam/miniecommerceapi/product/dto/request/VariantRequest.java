package com.sam.miniecommerceapi.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantRequest {
    @NotBlank(message = "PRODUCT_SKU_CANNOT_BLANK")
    @Size(min = 2, max = 16, message = "PRODUCT_SKU_SIZE")
    String sku;

    @NotNull(message = "PRODUCT_PRICE_CANNOT_BE_NULL")
    BigDecimal price;

    @NotNull(message = "PRODUCT_STOCK_QUANTITY_CANNOT_BE_NULL")
    Integer stockQuantity;

    @NotBlank(message = "PRODUCT_IMAGE_URL_CANNOT_BE_BLANK")
    @URL(message = "PRODUCT_IMAGE_URL_MUST_BE_URL")
    String imageUrl;

    Set<Long> attributeValueIds;
}
