package com.nexus.product_service.service;

import com.nexus.product_service.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto createProduct(ProductDto dto);
    ProductDto updateProduct(Long id, ProductDto dto);
    void deleteProduct(Long Id);
    ProductDto getProductById(Long id);
    ProductDto uploadImage(Long productId, MultipartFile file) throws IOException;
    Page<ProductDto> getALlProduct(int page, int size, String sortBy, String sortDir);
    Page<ProductDto> searchProduct(String keyword, int page, int size);
    Page<ProductDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, int page, int size);
    Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDir);


}
