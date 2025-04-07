package com.stlang.store.dao;

import com.stlang.store.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
