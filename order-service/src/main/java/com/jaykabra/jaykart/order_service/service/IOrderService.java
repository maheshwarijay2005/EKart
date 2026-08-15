package com.jaykabra.jaykart.order_service.service;

import com.jaykabra.jaykart.order_service.dto.OrderRequestDto;
import com.jaykabra.jaykart.order_service.dto.OrderResponseDto;
import com.jaykabra.jaykart.order_service.entity.Order;

import java.util.List;

public interface IOrderService {

    void createOrder(OrderRequestDto orderRequest);

    List<OrderResponseDto> getCustomerOrders();

    List<OrderResponseDto> getAllPendingOrders();

    void updateOrderStatus(Long orderId, String orderStatus);
}
