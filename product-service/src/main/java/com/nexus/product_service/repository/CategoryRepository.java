package com.nexus.product_service.repository;

import com.nexus.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long parentId);
    boolean existsByName(String name);
}
