package com.jaykabra.jaykart.catalog_service.service.impl;

import com.jaykabra.jaykart.catalog_service.dto.ProductDto;
import com.jaykabra.jaykart.catalog_service.entity.Product;
import com.jaykabra.jaykart.catalog_service.repository.ProductRepository;
import com.jaykabra.jaykart.catalog_service.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.jaykabra.jaykart.catalog_service.util.LRUCache;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final LRUCache<Long, ProductDto> localCache = new LRUCache<>(50);

    @Cacheable(value = "products", key = "'allProducts'")
    @Override
    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream().map(this::transformToDTO).collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        // First check custom LRU Cache (Memory Layer 1)
        if (localCache.containsKey(id)) {
            return localCache.get(id);
        }

        // Then check DB / Redis (Layer 2)
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        ProductDto dto = transformToDTO(product);
        
        // Put in local LRU cache
        localCache.put(id, dto);
        return dto;
    }

    private ProductDto transformToDTO(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        productDto.setProductId(product.getId());
        return productDto;
    }
}
