package com.jaykabra.jaykart.order_service.event;
import lombok.Data;
@Data
public class OrderEvent {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String status;
    private String message;
}
