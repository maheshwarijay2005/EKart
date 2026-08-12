package com.jaykabra.jaykart.service;

import com.jaykabra.jaykart.dto.ProductDto;

import java.util.List;

public interface IProductService {

    List<ProductDto> getProducts();
}
