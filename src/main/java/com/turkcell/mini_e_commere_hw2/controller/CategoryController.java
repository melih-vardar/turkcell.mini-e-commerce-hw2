package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.category.CreateCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.category.UpdateCategoryDto;
import com.turkcell.mini_e_commere_hw2.service.application.CategoryApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryApplicationService categoryApplicationService;

    public CategoryController(CategoryApplicationService categoryApplicationService) {
        this.categoryApplicationService = categoryApplicationService;
    }

    @PostMapping
    public void add(@RequestBody @Valid CreateCategoryDto createCategoryDto) {
        categoryApplicationService.add(createCategoryDto);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateCategoryDto updateCategoryDto) {
        categoryApplicationService.update(updateCategoryDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryApplicationService.delete(id);
    }

    @GetMapping
    public List<CategoryListingDto> getAll() {
        return categoryApplicationService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryListingDto getById(@PathVariable Integer id) {
        return categoryApplicationService.getById(id);
    }

    @PutMapping("/addSubCategory")
    public void addSubCategory(@RequestBody @Valid Integer categoryId, Integer subCategoryId) {}
}
