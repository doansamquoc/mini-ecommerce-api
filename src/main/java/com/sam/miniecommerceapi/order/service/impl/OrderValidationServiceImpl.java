package com.sam.miniecommerceapi.order.service.impl;

import com.sam.miniecommerceapi.common.config.AppProperties;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.enums.OrderStatus;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.order.dto.request.CancelOrderRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderCreationRequest;
import com.sam.miniecommerceapi.order.dto.request.OrderItemRequest;
import com.sam.miniecommerceapi.order.entity.Order;
import com.sam.miniecommerceapi.order.service.OrderValidationService;
import com.sam.miniecommerceapi.product.entity.ProductVariant;
import com.sam.miniecommerceapi.product.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderValidationServiceImpl implements OrderValidationService {
    AppProperties appProperties;
    ProductVariantService variantService;

    /**
     * Orders can only be canceled while {@link OrderStatus} is {@code PENDING} or {@code CONFIRMED}
     *
     * @param order   {@link Order}
     * @param request {@link CancelOrderRequest}
     */
    @Override
    public void validateCancellation(Order order, CancelOrderRequest request) {
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }

        Instant cancelDeadline = order.getOrderDate().plusMillis(appProperties.getCancellationDeadline());
        if (Instant.now().isAfter(cancelDeadline) /*&& !hasAdminRole()*/) {
            throw new BusinessException(ErrorCode.ORDER_CANCELLATION_DEADLINE_PASSED);
        }
    }

    @Override
    public void validateDelivery(Order order) {
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_UPDATE);
        }
    }

    @Override
    public void validateOrderCreation(Long userId, OrderCreationRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException(ErrorCode.ORDER_EMPTY);
        }

        List<Long> variantIds = request.getItems().stream().map(OrderItemRequest::getVariantId).toList();

        Map<Long, ProductVariant> variantMap = variantService.findAllByIds(variantIds)
                .stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));

        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductVariant variant = variantMap.get(itemRequest.getVariantId());

            validateStock(variant, itemRequest.getQuantity());
        }
    }

    private void validateStock(ProductVariant variant, int quantity) {
        if (variant == null) throw new BusinessException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);

        if (variant.getStockQuantity() < quantity)
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
    }
}
