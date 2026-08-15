package com.jaykabra.jaykart.catalog_service.service;

import com.jaykabra.jaykart.catalog_service.dto.ProductDto;

import java.util.List;

public interface IProductService {

    List<ProductDto> getProducts();
    ProductDto getProductById(Long id);
}
