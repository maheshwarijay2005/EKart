package com.jaykabra.jaykart.order_service.service.impl;
import com.jaykabra.jaykart.order_service.event.OrderEvent;
import com.jaykabra.jaykart.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderSagaListener {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-succeeded", groupId = "order-group")
    @Transactional
    public void handlePaymentSucceeded(OrderEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("CONFIRMED");
            orderRepository.save(order);
        });
    }

    @KafkaListener(topics = {"inventory-failed", "payment-failed"}, groupId = "order-group")
    @Transactional
    public void handleSagaFailure(OrderEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("CANCELLED");
            orderRepository.save(order);
            // TODO: Add Email Notification logic here
            System.out.println("SEND EMAIL: Order " + order.getOrderId() + " cancelled. Reason: " + event.getMessage());
        });
    }
}
