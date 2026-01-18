package com.nexus.product_service.repository;

import com.nexus.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByPriceBetween(double min, double max, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%') ) ")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT P FROM Product P " +
            "WHERE (:keyword IS NULL OR LOWER(P.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) ) " +
            "AND (:categoryId IS NULL OR P.category.id=:categoryId) " +
            "AND (P.price BETWEEN :minPrice AND :maxPrice)")
    Page<Product> advanceFilter(@Param("keyword") String keyword,
                                @Param("categoryId") Long categoryId,
                                @Param("minPrice") Double minPrice,
                                @Param("maxPrice") Double maxPrice,
                                Pageable pageable);

}
