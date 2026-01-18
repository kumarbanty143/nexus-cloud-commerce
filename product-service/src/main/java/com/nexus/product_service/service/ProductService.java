package com.nexus.product_service.service;

import com.nexus.product_service.dto.ProductDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductDto createProduct(ProductDto dto);
    ProductDto updateProduct(Long id, ProductDto dto);
    void deleteProduct(Long Id);
    ProductDto getProductById(Long id);
    Page<ProductDto> getALlProduct(int page, int size, String sortBy, String sortDir);
    Page<ProductDto> searchProduct(String keyword, int page, int size);
    Page<ProductDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, int page, int size);
    Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDir);


}
