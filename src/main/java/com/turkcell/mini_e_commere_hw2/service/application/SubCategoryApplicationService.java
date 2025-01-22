package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.subcategory.CreateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.SubCategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.UpdateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryApplicationService {
    void add(CreateSubCategoryDto createSubCategoryDto);
    void update(UpdateSubCategoryDto updateSubCategoryDto);
    List<SubCategoryListingDto> getAll();
    List<SubCategoryListingDto> getAllByCategoryId(Integer categoryId);
    Optional<SubCategory> findById(Integer id);
}
