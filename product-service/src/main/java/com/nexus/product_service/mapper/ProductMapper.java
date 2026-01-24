package com.nexus.product_service.mapper;

import com.nexus.product_service.dto.ProductDto;
import com.nexus.product_service.entity.Category;
import com.nexus.product_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDto toDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .quantity(product.getQuantity())
                .brand(product.getBrand())
                .imageUrl(product.getImageUrl())
                .categoryId(
                        product.getCategory() != null ? product.getCategory().getId(): null
                )
                .build();
    }

    public Product toEntity(ProductDto productDto, Category category){
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .discountPrice(productDto.getDiscountPrice())
                .quantity(productDto.getQuantity())
                .brand(productDto.getBrand())
                .imageUrl(productDto.getImageUrl())
                .category(category)
                .build();
    }
}
