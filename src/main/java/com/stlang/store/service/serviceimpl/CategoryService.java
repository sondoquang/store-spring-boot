package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.CategoryDAO;
import com.stlang.store.domain.Category;
import com.stlang.store.exception.DataExistingException;
import com.stlang.store.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Category create(Category category) {
        categoryDAO.findByName(category.getName()).orElseThrow(() -> new DataExistingException("Category Already Exist !"));
        return categoryDAO.save(category);
    }

    @Override
    public Category update(String name, Integer id) {
        Category oldCategory = categoryDAO.findById(id).orElseThrow(() -> new DataNotFoundException("Category Not Found !"));
        return categoryDAO.save(oldCategory);
    }

    @Override
    public void delete(Integer id) {
        Category category = findById(id);
        if(category != null) {
            categoryDAO.delete(category);
        }
    }
}
