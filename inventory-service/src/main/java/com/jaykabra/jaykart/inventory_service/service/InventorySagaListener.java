package com.jaykabra.jaykart.inventory_service.service;
import com.jaykabra.jaykart.inventory_service.entity.InventoryItem;
import com.jaykabra.jaykart.inventory_service.event.OrderEvent;
import com.jaykabra.jaykart.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventorySagaListener {
    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    @Transactional
    public void handleOrderCreated(OrderEvent event) {
        try {
            InventoryItem item = inventoryRepository.findByProductId(event.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found in inventory"));
            if (item.getAvailableQuantity() >= event.getQuantity()) {
                item.setAvailableQuantity(item.getAvailableQuantity() - event.getQuantity());
                inventoryRepository.save(item);
                event.setStatus("RESERVED");
                kafkaTemplate.send("inventory-reserved", event);
            } else {
                event.setStatus("FAILED");
                event.setMessage("Insufficient stock");
                kafkaTemplate.send("inventory-failed", event);
            }
        } catch (Exception e) { // Catches ObjectOptimisticLockingFailureException
            event.setStatus("FAILED");
            event.setMessage("Optimistic locking failure or error: " + e.getMessage());
            kafkaTemplate.send("inventory-failed", event);
        }
    }

    @KafkaListener(topics = "payment-failed", groupId = "inventory-group")
    @Transactional
    public void handlePaymentFailed(OrderEvent event) {
        inventoryRepository.findByProductId(event.getProductId()).ifPresent(item -> {
            item.setAvailableQuantity(item.getAvailableQuantity() + event.getQuantity());
            inventoryRepository.save(item);
        });
    }
}
