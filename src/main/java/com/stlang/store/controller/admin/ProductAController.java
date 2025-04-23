package com.stlang.store.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stlang.store.domain.Product;
import com.stlang.store.dto.ProductDTO;
import com.stlang.store.dto.ProductPageDTO;
import com.stlang.store.exception.DataIncorrectFormatException;
import com.stlang.store.service.IProductService;
import com.stlang.store.service.serviceimpl.FileManagerService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.path}")
public class    ProductAController {

    private final IProductService productService;
    private final FileManagerService fileManagerService;
    private final ObjectMapper objectMapper;

    public ProductAController(IProductService IProductService, FileManagerService fileManagerService, ObjectMapper objectMapper) {
        this.productService = IProductService;
        this.fileManagerService = fileManagerService;
        this.objectMapper = objectMapper;
    }

    @GetMapping({"/products"})
    public ResponseEntity<ProductPageDTO> getProducts(
            @PathParam("pageNo") Optional<Integer> pageNo,
            @PathParam("pageSize") Optional<Integer> pageSize,
            @PathParam("name") Optional<String> name,
            @PathParam("author") Optional<String> author,
            @PathParam("sort") Optional<String> sort,
            @PathParam("filter") Optional<String> filter,
            @PathParam("range") Optional<String> range
                                                   ) {
        int pageNoValue = pageNo.isPresent() ? pageNo.get() - 1 : 0;
        Map<String, String> queryParams = new HashMap<>();
        if (name.isPresent()) {
            queryParams.put("name", name.get());
        }
        if (author.isPresent()) {
            queryParams.put("author", author.get());
        }
        if(sort.isPresent()) {
            queryParams.put("sort", sort.get());
        }
        if(filter.isPresent()) {
            queryParams.put("filter", filter.get());
        }
        if(range.isPresent()) {
            queryParams.put("range", range.get());
        }

        Page<Product> pages =  productService.findAll(pageNoValue, pageSize.orElse(5), queryParams);

        ProductPageDTO productPageDTO = new ProductPageDTO();
        productPageDTO.setProducts(pages.getContent());
        ProductPageDTO.Meta meta = new ProductPageDTO.Meta();
        meta.setCurrentPage(pages.getNumber() + 1);
        meta.setPageSize(pages.getSize());
        meta.setTotalPages(pages.getTotalPages());
        meta.setTotalElements(Integer.valueOf(pages.getTotalElements() + ""));
        productPageDTO.setMeta(meta);
        return ResponseEntity.status(OK).body(productPageDTO);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = productService.findById(id);
        return ResponseEntity.status(OK).body(product);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
           throw new DataIncorrectFormatException("Data incorrect format for create product !");
        }
        Product createdProduct = productService.create(productDTO);
        return ResponseEntity.status(CREATED).body(createdProduct);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Integer productId,
                                                 @Valid @RequestBody Product product,
                                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new DataIncorrectFormatException("Data incorrect format for update product !");
        }
        Product updatedProduct = productService.update(product);
        return ResponseEntity.status(OK).body(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer productId) {
        productService.delete(productId);
        return ResponseEntity.status(NO_CONTENT).body(null);
    }

}
