package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.service.domain.CategoryService;
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
        Category category = categoryService.getByIdWithDetails(id);
        return modelMapper.map(category, CategoryListingDto.class);
    }

    @Override
    public void addSubCategory(Integer parentId, CreateCategoryDto createCategoryDto) {
        Category subCategory = modelMapper.map(createCategoryDto, Category.class);
        categoryService.addSubCategory(parentId, subCategory);
    }

    @Override
    public List<CategoryListingDto> getAllSubCategories(Integer parentId) {
        List<Category> subCategories = categoryService.getAllSubCategories(parentId);
        return subCategories.stream()
                .map(category -> modelMapper.map(category, CategoryListingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryListingDto> getCategoryTree() {
        List<Category> allCategories = categoryService.getAll();
        
        // Get root categories (categories without parent)
        List<Category> rootCategories = allCategories.stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());

        // Convert to DTOs with recursive subcategory mapping
        return rootCategories.stream()
                .map(this::mapCategoryToTreeDto)
                .collect(Collectors.toList());
    }

    private CategoryListingDto mapCategoryToTreeDto(Category category) {
        CategoryListingDto dto = modelMapper.map(category, CategoryListingDto.class);
        
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            dto.setSubCategories(
                category.getSubCategories().stream()
                    .map(this::mapCategoryToTreeDto)
                    .collect(Collectors.toList())
            );
        } else {
            dto.setSubCategories(new ArrayList<>());
        }
        
        dto.setProductCount(category.getProducts() != null ? category.getProducts().size() : 0);
        
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }
        
        return dto;
    }
}
