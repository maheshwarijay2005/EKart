package com.jaykabra.jaykart.order_service.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDto(Long orderId, String status,
                               BigDecimal totalPrice, String createdAt,
                               List<OrderItemReponseDto> items) {
}
