package com.nexus.product_service.impl;

import com.nexus.product_service.dto.ProductDto;
import com.nexus.product_service.entity.Category;
import com.nexus.product_service.entity.Product;
import com.nexus.product_service.mapper.ProductMapper;
import com.nexus.product_service.repository.ProductRepository;
import com.nexus.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductDto dto) {
        Category category=null;
        if(dto.getCategoryId() !=null){
            category = new Category();
            category.setId(dto.getCategoryId());
        }
        Product product = productMapper.toEntity(dto, category);
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("No Such Product Found"));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        if(dto.getCategoryId()!=null){
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long Id) {
        productRepository.deleteById(Id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not Found"));
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> getALlProduct(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> searchProduct(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.searchProducts(keyword, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.advanceFilter(null, categoryId, minPrice, maxPrice, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.advanceFilter(keyword, categoryId,  minPrice, maxPrice, pageable);
        return productPage.map(productMapper::toDto);
    }
}
