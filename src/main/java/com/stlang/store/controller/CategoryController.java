package com.stlang.store.controller;

import com.stlang.store.domain.Category;
import com.stlang.store.exception.DataNotFoundException;
import com.stlang.store.service.ICategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v01")
public class CategoryController {

    private final ICategoryService ICategoryService;

    public CategoryController(ICategoryService ICategoryService) {
        this.ICategoryService = ICategoryService;
    }


    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
       List<Category> categories = ICategoryService.findAll();
       return ResponseEntity.status(OK).body(categories);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable int id) {
        try {
            Category category = ICategoryService.findById(id);
            return ResponseEntity.status(OK).body(category);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        try {
            ICategoryService.create(category);
            return ResponseEntity.status(CREATED).body(category);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody String category) {
        try {
            Category updateCategory =  ICategoryService.update(category, id);
            return ResponseEntity.status(OK).body(updateCategory);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        try {
            ICategoryService.delete(id);
            return ResponseEntity.status(NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

}
