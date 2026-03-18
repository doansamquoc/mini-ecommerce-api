package com.sam.miniecommerceapi.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum PaymentMethod {
    @JsonProperty("COD")
    COD,
    @JsonProperty("PAYPAL")
    PAYPAL,
    @JsonProperty("STRIPE")
    STRIPE
}
