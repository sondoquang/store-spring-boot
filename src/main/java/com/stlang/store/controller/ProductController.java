package com.stlang.store.controller;

import com.stlang.store.domain.Product;
import com.stlang.store.exception.DataNotFoundException;
import com.stlang.store.service.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v01")
public class ProductController {

    private final IProductService IProductService;

    public ProductController(IProductService IProductService) {
        this.IProductService = IProductService;
    }

    @GetMapping({"/products", "/products/category/{id}"})
    public ResponseEntity<Page<Product>> getProducts(
            @PathVariable Optional<Integer> id,
            @RequestParam("page") Optional<Integer> pageNumber
                                                   ) {
        int pageNo = pageNumber.orElse(1);
        Page<Product> products = IProductService.findAll(id.orElse(null),pageNo,6);
        return ResponseEntity.status(OK).body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        try {
            Product product = IProductService.findById(id);
            return ResponseEntity.status(OK).body(product);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }
}
