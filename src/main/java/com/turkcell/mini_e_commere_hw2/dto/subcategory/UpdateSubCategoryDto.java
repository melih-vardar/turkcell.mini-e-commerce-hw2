package com.turkcell.mini_e_commere_hw2.dto.subcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

public class UpdateSubCategoryDto {
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    public UpdateSubCategoryDto() {
    }

    public UpdateSubCategoryDto(Integer id, String name, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}