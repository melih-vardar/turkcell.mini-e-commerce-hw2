package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.subcategory.CreateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.SubCategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.UpdateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.SubCategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryBusinessRules categoryBusinessRules;

    @Override
    public void add(CreateSubCategoryDto createSubCategoryDto) {
        categoryBusinessRules.categoryMustExist(createSubCategoryDto.getCategoryId());

        Category category = categoryRepository
                .findById(createSubCategoryDto.getCategoryId())
                .orElse(null);

        SubCategory subCategoryWithSameName = subCategoryRepository
                .findByName(createSubCategoryDto.getName())
                .orElse(null);

        if (subCategoryWithSameName != null)
            throw new BusinessException("SubCategory already exists");

        SubCategory subCategory = new SubCategory();
        subCategory.setName(createSubCategoryDto.getName());
        subCategory.setCategory(category);

        subCategoryRepository.save(subCategory);
    }

    @Override
    public void update(UpdateSubCategoryDto updateSubCategoryDto) {
        categoryBusinessRules.categoryMustExist(updateSubCategoryDto.getCategoryId());

        Category category = categoryRepository
                .findById(updateSubCategoryDto.getCategoryId())
                .orElse(null);

        SubCategory subCategoryWithSameName = subCategoryRepository
                .findByNameIsAndIdIsNot(updateSubCategoryDto.getName(), updateSubCategoryDto.getId())
                .orElse(null);

        if (subCategoryWithSameName != null)
            throw new BusinessException("SubCategory already exists");

        SubCategory subCategoryToUpdate = subCategoryRepository.findById(updateSubCategoryDto.getId())
                .orElseThrow(() -> new BusinessException("SubCategory not found"));

        subCategoryToUpdate.setName(updateSubCategoryDto.getName());
        subCategoryToUpdate.setCategory(category);

        subCategoryRepository.save(subCategoryToUpdate);
    }

    @Override
    public List<SubCategoryListingDto> getAll() {
        return subCategoryRepository
                .findAll()
                .stream()
                .map(subCategory -> new SubCategoryListingDto(subCategory.getId(), subCategory.getName(), subCategory.getCategory().getId(), subCategory.getCategory().getName()))
                .toList();
    }

    @Override
    public List<SubCategoryListingDto> getAllByCategoryId(Integer categoryId) {
        return subCategoryRepository
                .findAllByCategoryId(categoryId)
                .stream()
                .map(subCategory -> new SubCategoryListingDto(subCategory.getId(), subCategory.getName(), subCategory.getCategory().getId(), subCategory.getCategory().getName()))
                .toList();
    }

    @Override
    public Optional<SubCategory> findById(Integer id) {
        return subCategoryRepository.findById(id);
    }
} 