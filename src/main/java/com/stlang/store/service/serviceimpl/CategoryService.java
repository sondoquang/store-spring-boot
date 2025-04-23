package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.CategoryDAO;
import com.stlang.store.domain.Category;
import com.stlang.store.exception.DataExistingException;
import com.stlang.store.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements com.stlang.store.service.ICategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }


    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Category findById(Integer id) {
        return categoryDAO.findById(id).orElseThrow(() -> new DataNotFoundException("Category Not Found !"));
    }

    @Override
    public Category findByName(String name) {
        return categoryDAO.findByName(name).orElseThrow(() -> new DataNotFoundException("Category Not Found !"));
    }

    @Override
    public Category create(String categoryName) {
        Optional<Category> existingCategories = categoryDAO.findByName(categoryName);
        if (!existingCategories.isPresent()) {
            Category saveCategory = new Category();
            saveCategory.setName(categoryName);
            return categoryDAO.save(saveCategory);
        }
        throw new DataExistingException("Category Already Exist with name: " + categoryName);
    }

    @Override
    public Category update(String name, Integer id) {
        Optional<Category> existingCategory = categoryDAO.findById(id);
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setName(name);
            return categoryDAO.save(category);
        }
        throw new DataNotFoundException("Category Not Found !");
    }

    @Override
    public void delete(Integer id) {
        Category category = findById(id);
        if (category != null) {
            categoryDAO.delete(category);
        }

    }
}
