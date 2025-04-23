package com.stlang.store.service;

import com.stlang.store.domain.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> findAll();
    Category findById(Integer id);
    Category findByName(String name);
    Category create(String categoryName);
    Category update(String name, Integer id);
    void delete(Integer id);

}
