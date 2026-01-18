package com.nexus.product_service.mapper;

import com.nexus.product_service.dto.CategoryDto;
import com.nexus.product_service.entity.Category;

public class CategoryMapper {
    public CategoryDto toDto(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParentId())
                .build();
    }

    public Category toEntity(CategoryDto categoryDto){
        return Category.builder()
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .parentId(categoryDto.getParentId())
                .build();
    }
}
