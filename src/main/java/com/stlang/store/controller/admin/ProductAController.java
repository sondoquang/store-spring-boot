package com.stlang.store.controller.admin;

import com.stlang.store.domain.Product;
import com.stlang.store.service.IProductService;
import com.stlang.store.service.serviceimpl.FileManagerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.path}")
public class ProductAController {

    private final IProductService productService;
    private final FileManagerService fileManagerService;

    public ProductAController(IProductService IProductService, FileManagerService fileManagerService) {
        this.productService = IProductService;
        this.fileManagerService = fileManagerService;
    }

    @GetMapping({"/products", "/products/category/{id}"})
    public ResponseEntity<Page<Product>> getProducts(
            @PathVariable Optional<Integer> id,
            @RequestParam("page") Optional<Integer> pageNumber
                                                   ) {
        int pageNo = pageNumber.orElse(1);
        Page<Product> products = productService.findAll(id.orElse(null),pageNo,6);
        return ResponseEntity.status(OK).body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = productService.findById(id);
        return ResponseEntity.status(OK).body(product);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
        Product createdProduct = productService.create(product);
        return ResponseEntity.status(CREATED).body(createdProduct);
    }

    @PutMapping("/products")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
        Product updatedProduct = productService.update(product);
        return ResponseEntity.status(OK).body(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer productId) {
        productService.delete(productId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/products/upload/image")
    public ResponseEntity<String> uploadImage(@RequestParam("photo") MultipartFile file) {
        boolean isImage = this.isImageFile(file);
        if(isImage) {
            String fileName = fileManagerService.upload("products",file);
            return ResponseEntity.status(OK).body(fileName);
        }
        return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE).body(null);
    }


    private Boolean isImageFile(MultipartFile file) {
        if(file.getContentType() == null && !file.getContentType().equals("image/")) {
            return false;
        }
        return true;
    }
}
