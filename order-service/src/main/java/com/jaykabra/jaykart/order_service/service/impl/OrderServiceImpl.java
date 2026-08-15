package com.jaykabra.jaykart.order_service.service.impl;

import com.jaykabra.jaykart.order_service.dto.OrderItemReponseDto;
import com.jaykabra.jaykart.order_service.dto.OrderRequestDto;
import com.jaykabra.jaykart.order_service.dto.OrderResponseDto;
import com.jaykabra.jaykart.order_service.entity.Order;
import com.jaykabra.jaykart.order_service.entity.OrderItem;
import com.jaykabra.jaykart.order_service.repository.OrderRepository;
import com.jaykabra.jaykart.order_service.service.IOrderService;
import com.jaykabra.jaykart.order_service.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final HttpServletRequest request;

    @Override
    public void createOrder(OrderRequestDto orderRequest) {
        Long customerId = 1L; // Fallback
        String usernameHeader = request.getHeader("X-Auth-Username");
        if (usernameHeader != null) {
            // For simplicity, we just use a hash or mock id if username is present.
            // In a real app we'd get customerId from a token claim.
            customerId = (long) Math.abs(usernameHeader.hashCode());
        }

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setPaymentId("PENDING");
        order.setPaymentStatus("PENDING");
        order.setOrderStatus("CREATED");
        order.setTotalPrice(orderRequest.totalPrice());
        
        List<OrderItem> orderItems = orderRequest.items().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(item.productId());
            orderItem.setQuantity(item.quantity());
            orderItem.setPrice(item.price());
            return orderItem;
        }).collect(Collectors.toList());
        
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Saga pattern: emit event to trigger Inventory and Payment
        OrderEvent event = new OrderEvent();
        event.setOrderId(savedOrder.getOrderId());
        if (!orderItems.isEmpty()) {
            event.setProductId(orderItems.get(0).getProductId());
            event.setQuantity(orderItems.get(0).getQuantity());
        }
        event.setStatus("CREATED");
        kafkaTemplate.send("order-created", event);
    }

    @Override
    public List<OrderResponseDto> getCustomerOrders() {
        Long customerId = 1L; // Mock for now
        List<Order> orders = orderRepository.findOrdersByCustomerId(customerId);
        return orders.stream().map(this::mapToOrderResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getAllPendingOrders() {
        List<Order> orders = orderRepository.findOrdersByStatus("CREATED");
        return orders.stream().map(this::mapToOrderResponseDTO).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(Long orderId, String orderStatus) {
        orderRepository.updateOrderStatus(orderId, orderStatus, "system");
    }

    private OrderResponseDto mapToOrderResponseDTO(Order order) {
        List<OrderItemReponseDto> itemDTOs = order.getOrderItems().stream()
                .map(this::mapToOrderItemResponseDTO)
                .collect(Collectors.toList());
        return new OrderResponseDto(order.getOrderId(), order.getOrderStatus(), 
                order.getTotalPrice(), order.getCreatedAt().toString(), itemDTOs);
    }

    private OrderItemReponseDto mapToOrderItemResponseDTO(OrderItem orderItem) {
        // Missing product info since it's in another microservice; stubbing it
        return new OrderItemReponseDto(
                "Product-" + orderItem.getProductId(), 
                orderItem.getQuantity(),
                orderItem.getPrice(), 
                "default.png");
    }
}
