package com.sam.miniecommerceapi.common.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
    PENDING, SHIPPING, DELIVERED, CANCELLED
}
