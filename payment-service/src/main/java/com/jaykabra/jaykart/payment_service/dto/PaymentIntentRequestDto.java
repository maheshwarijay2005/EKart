package com.jaykabra.jaykart.payment_service.dto;

public record PaymentIntentRequestDto(Long amount, String currency) {
}
