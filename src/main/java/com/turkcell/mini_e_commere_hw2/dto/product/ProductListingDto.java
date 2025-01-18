package com.turkcell.mini_e_commere_hw2.dto.product;

import com.turkcell.mini_e_commere_hw2.entity.Product;
import lombok.*;

import java.math.BigDecimal;

public class ProductListingDto {
    private Integer id;
    private String name;
    private BigDecimal unitPrice;
    private int stock;
    private Integer subCategoryId;
    private String subCategoryName;
    private Integer categoryId;
    private String categoryName;
    private String description;
    private String image;

    public ProductListingDto() {
    }

    public ProductListingDto(Integer id, String name, BigDecimal unitPrice, int stock, Integer subCategoryId, String subCategoryName, Integer categoryId, String categoryName, String description, String image) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.image = image;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product toEntity() {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setUnitPrice(this.unitPrice);
        product.setStock(this.stock);
        return product;
    }
}
