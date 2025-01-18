package com.turkcell.mini_e_commere_hw2.dto.subcategory;

import lombok.*;


public class SubCategoryListingDto {
    private Integer id;
    private String name;
    private Integer categoryId;
    private String categoryName;

    public SubCategoryListingDto() {
    }

    public SubCategoryListingDto(Integer id, String name, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}