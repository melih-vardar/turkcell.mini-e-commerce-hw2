package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.service.domain.CategoryService;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryApplicationServiceImpl implements CategoryApplicationService {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Override
    public void add(CreateCategoryDto createCategoryDto) {
        Category category = modelMapper.map(createCategoryDto, Category.class);
        categoryService.add(category);
    }

    @Override
    public void update(UpdateCategoryDto updateCategoryDto) {
        Category category = modelMapper.map(updateCategoryDto, Category.class);
        categoryService.update(category);
    }

    @Override
    public void delete(Integer id) {
        categoryService.delete(id);
    }

    @Override
    public List<CategoryListingDto> getAll() {
        List<Category> categories = categoryService.getAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryListingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryListingDto getById(Integer id) {
        Category category = categoryService.getById(id);
        return modelMapper.map(category, CategoryListingDto.class);
    }

    @Override
    public void addSubCategory(Integer categoryId, Integer subCategoryId) {
        categoryService.addSubCategory(categoryId, subCategoryId);
    }
}
