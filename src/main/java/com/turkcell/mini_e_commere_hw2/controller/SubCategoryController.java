package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.subcategory.CreateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.SubCategoryListingDto;
import com.turkcell.mini_e_commere_hw2.dto.subcategory.UpdateSubCategoryDto;
import com.turkcell.mini_e_commere_hw2.service.domain.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sub-categories")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping
    public void add(@RequestBody @Valid CreateSubCategoryDto createSubCategoryDto) {
        subCategoryService.add(createSubCategoryDto);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateSubCategoryDto updateSubCategoryDto) {
        subCategoryService.update(updateSubCategoryDto);
    }

    @GetMapping
    public List<SubCategoryListingDto> getAll() {
        return subCategoryService.getAll();
    }

    @GetMapping("/by-category/{categoryId}")
    public List<SubCategoryListingDto> getAllByCategoryId(@PathVariable Integer categoryId) {
        return subCategoryService.getAllByCategoryId(categoryId);
    }
} 