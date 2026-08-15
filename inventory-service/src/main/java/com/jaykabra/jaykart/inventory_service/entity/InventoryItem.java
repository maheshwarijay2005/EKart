package com.jaykabra.jaykart.inventory_service.entity;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "INVENTORY")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer availableQuantity;
    
    @Version
    private Integer version;
}
