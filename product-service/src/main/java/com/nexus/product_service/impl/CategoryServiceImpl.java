package com.nexus.product_service.impl;

import com.nexus.product_service.dto.CategoryDto;
import com.nexus.product_service.entity.Category;
import com.nexus.product_service.mapper.CategoryMapper;
import com.nexus.product_service.repository.CategoryRepository;
import com.nexus.product_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category category=categoryMapper.toEntity(dto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto).orElseThrow(()->new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream().map(categoryMapper::toDto).toList();
    }
}
