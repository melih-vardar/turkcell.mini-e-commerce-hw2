package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.repository.SubCategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryBusinessRules categoryBusinessRules;
    @Mock
    private SubCategoryRepository subCategoryRepository;
    @Mock
    private ProductRepository productRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, categoryBusinessRules, 
                subCategoryRepository, productRepository);
    }

    @Test
    void add_WithValidData_ShouldSucceed() {
        // Arrange
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Test Category");

        when(categoryRepository.findByName(createCategoryDto.getName())).thenReturn(Optional.empty());

        // Act
        categoryService.add(createCategoryDto);

        // Assert
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void add_WithExistingName_ShouldThrowException() {
        // Arrange
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Existing Category");

        Category existingCategory = new Category();
        existingCategory.setName(createCategoryDto.getName());

        when(categoryRepository.findByName(createCategoryDto.getName())).thenReturn(Optional.of(existingCategory));

        // Act & Assert
        assertThrows(BusinessException.class, () -> categoryService.add(createCategoryDto));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void update_WithValidData_ShouldSucceed() {
        // Arrange
        Integer categoryId = 1;
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setId(categoryId);
        updateCategoryDto.setName("Updated Category");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Original Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // Act
        categoryService.update(updateCategoryDto);

        // Assert
        verify(categoryBusinessRules).categoryMustExist(categoryId);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer categoryId = 999;
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto();
        updateCategoryDto.setId(categoryId);
        updateCategoryDto.setName("Updated Category");

        doThrow(new BusinessException("Category not found"))
            .when(categoryBusinessRules).categoryMustExist(categoryId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> categoryService.update(updateCategoryDto));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void delete_WithValidId_ShouldSucceed() {
        // Arrange
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryBusinessRules).categoryMustExist(categoryId);
        doNothing().when(categoryBusinessRules).categoryMustNotHaveAssociatedProducts(categoryId);

        // Act
        categoryService.delete(categoryId);

        // Assert
        verify(categoryBusinessRules).categoryMustExist(categoryId);
        verify(categoryBusinessRules).categoryMustNotHaveAssociatedProducts(categoryId);
        verify(categoryRepository).delete(category);
    }

    @Test
    void delete_WithAssociatedProducts_ShouldThrowException() {
        // Arrange
        Integer categoryId = 1;

        doNothing().when(categoryBusinessRules).categoryMustExist(categoryId);
        doThrow(new BusinessException("Category has associated products"))
            .when(categoryBusinessRules).categoryMustNotHaveAssociatedProducts(categoryId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> categoryService.delete(categoryId));
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void getAll_ShouldReturnAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Category 1");
        
        SubCategory subCategory1 = new SubCategory();
        subCategory1.setId(1);
        subCategory1.setName("SubCategory 1");
        subCategory1.setCategory(category1);

        SubCategory subCategory2 = new SubCategory();
        subCategory2.setId(2);
        subCategory2.setName("SubCategory 2");
        subCategory2.setCategory(category1);

        List<SubCategory> subCategories1 = Arrays.asList(subCategory1, subCategory2);
        category1.setSubCategories(subCategories1);

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Category 2");
        category2.setSubCategories(new ArrayList<>());

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // Act
        List<CategoryListingDto> result = categoryService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        CategoryListingDto resultCategory1 = result.get(0);
        assertEquals(category1.getId(), resultCategory1.getId());
        assertEquals(category1.getName(), resultCategory1.getName());
        assertEquals(2, resultCategory1.getSubCategoryCount());
        assertEquals(2, resultCategory1.getSubCategoryName().size());
        assertTrue(resultCategory1.getSubCategoryName().contains(subCategory1.getName()));
        assertTrue(resultCategory1.getSubCategoryName().contains(subCategory2.getName()));

        CategoryListingDto resultCategory2 = result.get(1);
        assertEquals(category2.getId(), resultCategory2.getId());
        assertEquals(category2.getName(), resultCategory2.getName());
        assertEquals(0, resultCategory2.getSubCategoryCount());
        assertTrue(resultCategory2.getSubCategoryName().isEmpty());
    }

    @Test
    void getById_WithValidId_ShouldReturnCategory() {
        // Arrange
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");
        category.setSubCategories(new ArrayList<>());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        CategoryListingDto result = categoryService.getById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(0, result.getSubCategoryCount());
        assertTrue(result.getSubCategoryName().isEmpty());
    }

    @Test
    void getById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer categoryId = 999;
        doThrow(new BusinessException("Category not found"))
            .when(categoryBusinessRules).categoryMustExist(categoryId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> categoryService.getById(categoryId));
    }

    @Test
    void addSubCategory_WithValidData_ShouldSucceed() {
        // Arrange
        Integer categoryId = 1;
        Integer subCategoryId = 1;

        Category category = new Category();
        category.setId(categoryId);
        category.setSubCategories(new ArrayList<>());

        SubCategory subCategory = new SubCategory();
        subCategory.setId(subCategoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(subCategoryRepository.findById(subCategoryId)).thenReturn(Optional.of(subCategory));

        // Act
        categoryService.addSubCategory(categoryId, subCategoryId);

        // Assert
        verify(categoryBusinessRules).categoryMustExist(subCategoryId);
        verify(categoryRepository).save(category);
        assertEquals(1, category.getSubCategories().size());
        assertTrue(category.getSubCategories().contains(subCategory));
    }

    @Test
    void addSubCategory_WithInvalidCategoryId_ShouldThrowException() {
        // Arrange
        Integer categoryId = 999;
        Integer subCategoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> categoryService.addSubCategory(categoryId, subCategoryId));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void addSubCategory_WithInvalidSubCategoryId_ShouldThrowException() {
        // Arrange
        Integer categoryId = 1;
        Integer subCategoryId = 999;

        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(subCategoryRepository.findById(subCategoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> categoryService.addSubCategory(categoryId, subCategoryId));
        verify(categoryRepository, never()).save(any());
    }
} 