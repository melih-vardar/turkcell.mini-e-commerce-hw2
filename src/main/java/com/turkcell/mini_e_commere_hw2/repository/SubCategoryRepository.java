package com.turkcell.mini_e_commere_hw2.repository;

import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
    Optional<SubCategory> findByName(String name);
    Optional<SubCategory> findByNameIsAndIdIsNot(String name, Integer id);
    List<SubCategory> findAllByCategoryId(Integer categoryId);
} 