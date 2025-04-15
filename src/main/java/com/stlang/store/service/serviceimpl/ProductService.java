package com.stlang.store.service.serviceimpl;

import com.stlang.store.dao.CategoryDAO;
import com.stlang.store.dao.ProductDAO;
import com.stlang.store.dto.ProductDTO;
import com.stlang.store.domain.Category;
import com.stlang.store.domain.Product;
import com.stlang.store.exception.DataNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements com.stlang.store.service.IProductService {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final ModelMapper modelMapper;

    public ProductService(ProductDAO productDAO, CategoryDAO categoryDAO, ModelMapper modelMapper) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public Page<Product> findAll(Integer categoryId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if(categoryId == null){
            return productDAO.findAll(pageable);
        }
        Category category = categoryDAO.findById(categoryId).orElse(null);
        if(category == null){
            return productDAO.findAll(pageable);
        }
        return productDAO.findByCategory(category, pageable);
    }

    @Override
    public Product findById(Integer id) {
        return productDAO.findById(id).orElseThrow(() -> {throw new DataNotFoundException("No product found with id: " + id);});
    }

    @Override
    public Product create(Product product) {
        return productDAO.save(product);
    }

    @Override
    public Product update(Product product) {
        Product existingProduct = this.findById(product.getId());
        if(product.getImage() == null){
            product.setImage(existingProduct.getImage());
        }
        return productDAO.save(product);
    }

    @Override
    public void delete(Integer id) {
        Product existingProduct = findById(id);
        existingProduct.setActive(false);
        productDAO.save(existingProduct);
    }

    @Override
    public ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getConvertProductDTOs(List<Product> products) {
        return products.stream().map(this::convertToDTO).toList();
    }
}
