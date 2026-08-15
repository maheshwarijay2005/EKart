package com.jaykabra.jaykart.order_service.dto;

import java.math.BigDecimal;

public record OrderItemReponseDto(String productName, Integer quantity,
                                  BigDecimal price, String imageUrl) {
}
