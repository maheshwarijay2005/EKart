package com.jaykabra.jaykart.catalog_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

import java.io.Serializable;

@Getter
@Setter
public class ProductDto implements Serializable {

    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer popularity;
    private String imageUrl;
    private Instant createdAt;

}
