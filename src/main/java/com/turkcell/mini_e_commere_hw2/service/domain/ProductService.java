package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void delete(Integer id);
    Product findById(Integer id);
    List<Product> getAll();
    List<Product> search(String categoryId, String subCategoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock);
    void add(Product product);
    void update(Product product);
}
