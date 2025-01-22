package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.subcategory.CreateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.SubCategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.UpdateSubCategoryDto;

import java.util.List;

public interface SubCategoryApplicationService {
    void add(CreateSubCategoryDto createSubCategoryDto);
    void update(UpdateSubCategoryDto updateSubCategoryDto);
    List<SubCategoryListingDto> getAll();
    List<SubCategoryListingDto> getAllByCategoryId(Integer categoryId);
}
