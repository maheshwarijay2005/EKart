package com.jaykabra.jaykart.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
