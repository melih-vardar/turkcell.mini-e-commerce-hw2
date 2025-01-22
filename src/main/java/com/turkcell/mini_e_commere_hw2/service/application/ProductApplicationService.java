package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductApplicationService 
{
    void add(CreateProductDto createProductDto);

    void update(UpdateProductDto updateProductDto);

    List<ProductListingDto> getAll();

    List<ProductListingDto> search(String categoryId, String subCategoryId, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock);

    ProductListingDto findById(Integer id);
    void delete(Integer id);
}
