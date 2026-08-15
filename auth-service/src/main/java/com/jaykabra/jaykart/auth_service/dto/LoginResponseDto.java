package com.jaykabra.jaykart.auth_service.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
