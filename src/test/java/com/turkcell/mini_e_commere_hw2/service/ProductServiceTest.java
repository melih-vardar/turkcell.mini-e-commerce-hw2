package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.entity.SubCategory;
import com.turkcell.mini_e_commere_hw2.entity.Category;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.rules.SubCategoryBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private SubCategoryService subCategoryService;
    @Mock
    private SubCategoryBusinessRules subCategoryBusinessRules;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, subCategoryService, subCategoryBusinessRules);
    }

    @Test
    void add_WithValidData_ShouldSucceed() {
        // Arrange
        CreateProductDto createProductDto = new CreateProductDto(
            "Test Product", 
            BigDecimal.valueOf(99.99), 
            10, 
            1, 
            "Test Description", 
            "test-image.jpg"
        );

        SubCategory mockSubCategory = new SubCategory();
        mockSubCategory.setId(1);
        when(subCategoryService.findById(createProductDto.getSubCategoryId())).thenReturn(Optional.of(mockSubCategory));
        when(productRepository.findByName(createProductDto.getName())).thenReturn(Optional.empty());

        // Act
        productService.add(createProductDto);

        // Assert
        verify(subCategoryBusinessRules).subCategoryMustExist(createProductDto.getSubCategoryId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void add_WithExistingName_ShouldThrowException() {
        // Arrange
        CreateProductDto createProductDto = new CreateProductDto(
            "Existing Product", 
            BigDecimal.valueOf(99.99), 
            10, 
            1, 
            null, 
            null
        );

        Product existingProduct = new Product();
        existingProduct.setName(createProductDto.getName());
        when(productRepository.findByName(createProductDto.getName())).thenReturn(Optional.of(existingProduct));

        // Act & Assert
        assertThrows(BusinessException.class, () -> productService.add(createProductDto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void add_WithInvalidSubCategory_ShouldThrowException() {
        // Arrange
        CreateProductDto createProductDto = new CreateProductDto(
            "Test Product", 
            BigDecimal.valueOf(99.99), 
            10, 
            999, // Non-existent ID
            null, 
            null
        );

        doThrow(new BusinessException("SubCategory not found"))
            .when(subCategoryBusinessRules).subCategoryMustExist(createProductDto.getSubCategoryId());

        // Act & Assert
        assertThrows(BusinessException.class, () -> productService.add(createProductDto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void update_WithValidData_ShouldSucceed() {
        // Arrange
        UpdateProductDto updateProductDto = new UpdateProductDto(
            1,
            "Updated Product",
            BigDecimal.valueOf(149.99),
            20,
            1,
            "Updated Description",
            "updated-image.jpg"
        );

        Product existingProduct = new Product();
        existingProduct.setId(1);
        existingProduct.setName("Original Product");
        
        SubCategory mockSubCategory = new SubCategory();
        mockSubCategory.setId(1);

        when(productRepository.findById(updateProductDto.getId())).thenReturn(Optional.of(existingProduct));
        when(subCategoryService.findById(updateProductDto.getSubCategoryId())).thenReturn(Optional.of(mockSubCategory));
        when(productRepository.findByNameIsAndIdIsNot(updateProductDto.getName(), updateProductDto.getId())).thenReturn(Optional.empty());

        // Act
        productService.update(updateProductDto);

        // Assert
        verify(subCategoryBusinessRules).subCategoryMustExist(updateProductDto.getSubCategoryId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void update_WithNonExistentProduct_ShouldThrowException() {
        // Arrange
        UpdateProductDto updateProductDto = new UpdateProductDto(
            999, // Non-existent ID
            "Updated Product",
            null,
            0,
            null,
            null,
            null
        );

        when(productRepository.findById(updateProductDto.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> productService.update(updateProductDto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void delete_WithValidId_ShouldSucceed() {
        // Arrange
        Integer productId = 1;
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.delete(productId);

        // Assert
        verify(productRepository).delete(product);
    }

    @Test
    void delete_WithNonExistentId_ShouldThrowException() {
        // Arrange
        Integer productId = 999;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> productService.delete(productId));
        verify(productRepository, never()).delete(any());
    }

    @Test
    void getAll_ShouldReturnAllProducts() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");

        SubCategory subCategory = new SubCategory();
        subCategory.setId(1);
        subCategory.setName("Test SubCategory");
        subCategory.setCategory(category);

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setUnitPrice(BigDecimal.valueOf(99.99));
        product1.setStock(10);
        product1.setSubCategory(subCategory);
        product1.setDescription("Test Description 1");
        product1.setImage("test-image-1.jpg");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setUnitPrice(BigDecimal.valueOf(149.99));
        product2.setStock(20);
        product2.setSubCategory(subCategory);
        product2.setDescription("Test Description 2");
        product2.setImage("test-image-2.jpg");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<ProductListingDto> result = productService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1.getName(), result.get(0).getName());
        assertEquals(product2.getName(), result.get(1).getName());
        assertEquals(category.getId(), result.get(0).getCategoryId());
        assertEquals(category.getName(), result.get(0).getCategoryName());
    }

    @Test
    void findById_WithValidId_ShouldReturnProduct() {
        // Arrange
        Integer productId = 1;
        
        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");

        SubCategory subCategory = new SubCategory();
        subCategory.setId(1);
        subCategory.setName("Test SubCategory");
        subCategory.setCategory(category);

        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setUnitPrice(BigDecimal.valueOf(99.99));
        product.setStock(10);
        product.setSubCategory(subCategory);
        product.setDescription("Test Description");
        product.setImage("test-image.jpg");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ProductListingDto result = productService.findById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getUnitPrice(), result.getUnitPrice());
        assertEquals(product.getStock(), result.getStock());
        assertEquals(subCategory.getId(), result.getSubCategoryId());
        assertEquals(subCategory.getName(), result.getSubCategoryName());
        assertEquals(category.getId(), result.getCategoryId());
        assertEquals(category.getName(), result.getCategoryName());
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer productId = 999;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> productService.findById(productId));
    }

    @Test
    void search_WithValidParameters_ShouldReturnFilteredProducts() {
        // Arrange
        String categoryId = "1";
        String subCategoryId = "1";
        BigDecimal minPrice = BigDecimal.valueOf(50);
        BigDecimal maxPrice = BigDecimal.valueOf(150);
        Boolean inStock = true;

        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");

        SubCategory subCategory = new SubCategory();
        subCategory.setId(1);
        subCategory.setName("Test SubCategory");
        subCategory.setCategory(category);

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setUnitPrice(BigDecimal.valueOf(99.99));
        product1.setStock(10);
        product1.setSubCategory(subCategory);
        product1.setDescription("Test Description");
        product1.setImage("test-image.jpg");

        when(productRepository.search(categoryId, subCategoryId, minPrice, maxPrice, inStock))
            .thenReturn(Arrays.asList(product1));

        // Act
        List<ProductListingDto> result = productService.search(categoryId, subCategoryId, minPrice, maxPrice, inStock);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product1.getName(), result.get(0).getName());
        assertEquals(product1.getUnitPrice(), result.get(0).getUnitPrice());
        assertEquals(category.getId(), result.get(0).getCategoryId());
        assertEquals(category.getName(), result.get(0).getCategoryName());
    }
} 