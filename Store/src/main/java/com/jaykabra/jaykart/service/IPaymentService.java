package com.jaykabra.jaykart.service;

import com.jaykabra.jaykart.dto.PaymentIntentRequestDto;
import com.jaykabra.jaykart.dto.PaymentIntentResponseDto;

public interface IPaymentService {

    PaymentIntentResponseDto createPaymentIntent(PaymentIntentRequestDto requestDto);
}
