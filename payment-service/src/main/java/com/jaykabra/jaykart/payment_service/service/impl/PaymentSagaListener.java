package com.jaykabra.jaykart.payment_service.service.impl;
import com.jaykabra.jaykart.payment_service.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentSagaListener {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @KafkaListener(topics = "inventory-reserved", groupId = "payment-group")
    public void handleInventoryReserved(OrderEvent event) {
        try {
            // Dummy Stripe Call
            boolean paymentSuccess = processStripePayment(event);
            if (paymentSuccess) {
                event.setStatus("PAID");
                kafkaTemplate.send("payment-succeeded", event);
            } else {
                event.setStatus("PAYMENT_FAILED");
                event.setMessage("Stripe Card Declined");
                kafkaTemplate.send("payment-failed", event);
            }
        } catch (Exception e) {
            event.setStatus("PAYMENT_ERROR");
            event.setMessage("Stripe API Error");
            kafkaTemplate.send("payment-failed", event);
        }
    }
    
    private boolean processStripePayment(OrderEvent event) {
        // Assume 90% success rate for simulation
        return Math.random() > 0.1;
    }
}
