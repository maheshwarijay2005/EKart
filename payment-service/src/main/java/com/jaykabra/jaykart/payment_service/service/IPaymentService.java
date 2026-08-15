package com.jaykabra.jaykart.payment_service.service;

import com.jaykabra.jaykart.payment_service.dto.PaymentIntentRequestDto;
import com.jaykabra.jaykart.payment_service.dto.PaymentIntentResponseDto;

public interface IPaymentService {

    PaymentIntentResponseDto createPaymentIntent(PaymentIntentRequestDto requestDto);
}
