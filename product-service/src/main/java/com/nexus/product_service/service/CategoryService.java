package com.nexus.product_service.service;

import com.nexus.product_service.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto dto);
    CategoryDto updateCategory(Long id, CategoryDto dto);
    void deleteCategory(Long id);
    CategoryDto getCategory(Long id);
    List<CategoryDto> getAllCategories();
}
