package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {
    void add(CreateCategoryDto createCategoryDto);
    void update(UpdateCategoryDto updateCategoryDto);
    void delete(Integer id);
    List<CategoryListingDto> getAll();
    CategoryListingDto getById(Integer id);
    void addSubCategory(Integer categoryId, Integer subCategoryId);
}
