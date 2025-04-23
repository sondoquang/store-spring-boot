package com.stlang.store.dao;

import com.stlang.store.domain.Category;
import com.stlang.store.domain.Product;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Integer> {
    Page<Product> findByActiveEquals(Boolean active,Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);

    Page<Product> findByNameContainingAndActiveEquals(String name, Boolean active, Pageable pageable);

    Page<Product> findByAuthorContainingAndActiveEquals(String fullname, Boolean active, Pageable pageable);

    Page<Product> findByNameContainingAndAuthorContainingAndActiveEquals(String name, Boolean active, String author, Pageable pageable);

    Page<Product> findByCategoryIdIn(List<String> categoryFilter, Pageable pageable);

    Page<Product> findByPriceBetween(Double from, Double to, Pageable pageable);

    Page<Product> findByPriceBetweenAndCategoryIdIn(Double from, Double to, List<String> categoryFilter, Pageable pageable);
}
