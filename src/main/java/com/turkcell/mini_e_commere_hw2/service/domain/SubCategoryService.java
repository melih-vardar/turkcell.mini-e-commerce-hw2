package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.SubCategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryService {
    void add(SubCategory subCategory);
    void update(SubCategory subCategory);
    List<SubCategory> getAll();
    List<SubCategory> getAllByCategoryId(Integer categoryId);
    Optional<SubCategory> findById(Integer id);
} 