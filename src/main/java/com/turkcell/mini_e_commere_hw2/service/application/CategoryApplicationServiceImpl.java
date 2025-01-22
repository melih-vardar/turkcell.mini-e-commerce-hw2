package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.service.domain.CategoryService;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryApplicationServiceImpl implements CategoryApplicationService {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryApplicationServiceImpl(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

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
}
