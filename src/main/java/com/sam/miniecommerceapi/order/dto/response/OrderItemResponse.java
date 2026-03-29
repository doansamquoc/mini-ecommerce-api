package com.sam.miniecommerceapi.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sam.miniecommerceapi.product.dto.response.ProductVariantResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    Long id;
    ProductVariantResponse variant;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
}
