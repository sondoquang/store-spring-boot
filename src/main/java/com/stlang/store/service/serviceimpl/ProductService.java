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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public Page<Product> findAll(int pageNumber, int pageSize, Map<String, String> queryParams, Sort... sort) {
        Pageable pageable;
        if(queryParams.containsKey("sort")){
            Sort newSort = null;
            String criteria = queryParams.get("sort");
            if(criteria.contains("-")){
                criteria = criteria.split("-")[1];
                newSort = Sort.by(Sort.Direction.DESC,criteria );
            }else{
                newSort = Sort.by(Sort.Direction.ASC,criteria );
            }
            pageable = PageRequest.of(pageNumber, pageSize, newSort);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        // Cho phần home //
        if(queryParams.containsKey("filter") && queryParams.containsKey("range")){
            String filter = queryParams.get("filter");
            List<String> categoryFilter = Arrays.asList(filter.split(","));
            String range = queryParams.get("range");
            List<String> priceFilter = Arrays.asList(range.split("-"));
            Double from = Double.parseDouble(priceFilter.get(1));
            Double to = Double.parseDouble(priceFilter.get(3));
            return productDAO.findByPriceBetweenAndCategoryIdIn(from,to,categoryFilter, pageable);
        }else{
            if(queryParams.containsKey("filter")){
                String filter = queryParams.get("filter");
                List<String> categoryFilter = Arrays.asList(filter.split(","));
                return productDAO.findByCategoryIdIn(categoryFilter, pageable);
            }
            if(queryParams.containsKey("range")){
                String range = queryParams.get("range");
                List<String> priceFilter = Arrays.asList(range.split("-"));
                Double from = Double.parseDouble(priceFilter.get(1));
                Double to = Double.parseDouble(priceFilter.get(3));
                return productDAO.findByPriceBetween(from, to, pageable);
            }
        }

        if(queryParams.size() < 2){
            if(queryParams.containsKey("name")){
                return productDAO.findByNameContainingAndActiveEquals(queryParams.get("name"), true, pageable);
            }
            if(queryParams.containsKey("author")){
                return productDAO.findByAuthorContainingAndActiveEquals(queryParams.get("author"), true, pageable);
            }
        }else if(queryParams.size() == 2){
            String name = queryParams.get("name");
            String author = queryParams.get("author");
            return productDAO.findByNameContainingAndAuthorContainingAndActiveEquals(name,true, author, pageable);
        }
        return productDAO.findByActiveEquals(true, pageable);
    }

    @Override
    public Product findById(Integer id) {
        return productDAO.findById(id).orElseThrow(() -> {throw new DataNotFoundException("No product found with id: " + id);});
    }

    @Override
    public Product create(ProductDTO productDTO) {
        Product saveProduct = modelMapper.map(productDTO, Product.class);
        // find category //
        Category category = categoryDAO.findById(productDTO.getCategoryId())
                .orElseThrow(() -> {throw new DataNotFoundException("No Category found with id: " + productDTO.getCategoryId());});
        saveProduct.setCategory(category);
        return productDAO.save(saveProduct);
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
