package com.sam.miniecommerceapi.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartCreationRequest(
        @NotNull(message = "message.validation.id_required")
        Long userId
) {}
