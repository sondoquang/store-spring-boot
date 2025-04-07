package com.stlang.store.service;

import com.stlang.store.dto.ProductDTO;
import com.stlang.store.domain.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    List<Product> findAll();
    Page<Product> findAll(Integer category, int pageNo, int pageSize);
    Product findById(Integer id);
    Product create(Product product);
    Product update(Product product);
    void delete(Integer id);
    ProductDTO convertToDTO(Product product);
    List<ProductDTO> getConvertProductDTOs(List<Product> products);
}
