package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.service.application.CategoryApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryApplicationService categoryApplicationService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        categoryApplicationService.add(createCategoryDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryDto updateCategoryDto) {
        updateCategoryDto.setId(id);
        categoryApplicationService.update(updateCategoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryApplicationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryListingDto>> getAllCategories() {
        List<CategoryListingDto> categories = categoryApplicationService.getAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryListingDto> getCategoryById(@PathVariable Integer id) {
        CategoryListingDto category = categoryApplicationService.getById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryListingDto>> getSubCategories(@PathVariable Integer id) {
        List<CategoryListingDto> subCategories = categoryApplicationService.getAllSubCategories(id);
        return ResponseEntity.ok(subCategories);
    }

    @PostMapping("/{parentId}/subcategories")
    public ResponseEntity<Void> addSubCategory(
            @PathVariable Integer parentId,
            @Valid @RequestBody CreateCategoryDto createCategoryDto) {
        categoryApplicationService.addSubCategory(parentId, createCategoryDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryListingDto>> getCategoryTree() {
        List<CategoryListingDto> categoryTree = categoryApplicationService.getCategoryTree();
        return ResponseEntity.ok(categoryTree);
    }
}
