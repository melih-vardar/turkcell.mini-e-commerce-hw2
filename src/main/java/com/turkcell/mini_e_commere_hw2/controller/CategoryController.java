package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public void add(@RequestBody @Valid CreateCategoryDto createCategoryDto) {
        categoryService.add(createCategoryDto);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateCategoryDto updateCategoryDto) {
        categoryService.update(updateCategoryDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }

    @GetMapping
    public List<CategoryListingDto> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryListingDto getById(@PathVariable Integer id) {
        return categoryService.getById(id);
    }

    @PutMapping("/addSubCategory")
    public void addSubCategory(@RequestBody @Valid Integer categoryId, Integer subCategoryId) {}
}
