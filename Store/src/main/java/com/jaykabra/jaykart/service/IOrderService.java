package com.jaykabra.jaykart.service;

import com.jaykabra.jaykart.dto.OrderRequestDto;
import com.jaykabra.jaykart.dto.OrderResponseDto;
import com.jaykabra.jaykart.entity.Order;

import java.util.List;

public interface IOrderService {

    void createOrder(OrderRequestDto orderRequest);

    List<OrderResponseDto> getCustomerOrders();

    List<OrderResponseDto> getAllPendingOrders();

    void updateOrderStatus(Long orderId, String orderStatus);
}
