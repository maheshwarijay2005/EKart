package com.jaykabra.jaykart.controller;

import com.jaykabra.jaykart.dto.ErrorResponseDto;
import com.jaykabra.jaykart.dto.ProductDto;
import com.jaykabra.jaykart.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService iProductService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> productList = iProductService.getProducts();
        return ResponseEntity.ok().body(productList);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
            WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false), HttpStatus.SERVICE_UNAVAILABLE,
                exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
