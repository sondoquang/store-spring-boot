package com.stlang.store.service;

import com.stlang.store.dto.ProductDTO;
import com.stlang.store.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface IProductService {
    List<Product> findAll();
    Page<Product> findAll(int pageNo, int pageSize, Map<String,String> queryParams, Sort... sort);
    Product findById(Integer id);
    Product create(ProductDTO product);
    Product update(Product product);
    void delete(Integer id);
    ProductDTO convertToDTO(Product product);
    List<ProductDTO> getConvertProductDTOs(List<Product> products);
}
