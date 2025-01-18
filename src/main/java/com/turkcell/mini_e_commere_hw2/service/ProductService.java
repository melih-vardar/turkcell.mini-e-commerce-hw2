package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void add(CreateProductDto createProductDto);

    void update(UpdateProductDto updateProductDto);

    List<ProductListingDto> getAll();

    List<ProductListingDto> search(String categoryId, BigDecimal minPrice, BigDecimal maxPrice, boolean inStock);
    ProductListingDto findById(Integer id);
}
