package com.turkcell.mini_e_commere_hw2.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDto {
    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, max = 50, message = "Name must be at least 3 characters and at most 50 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than 0")
    private BigDecimal unitPrice;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;

    @NotNull(message = "SubCategory ID cannot be null")
    private Integer subCategoryId;

    @Length(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Length(max = 255, message = "Image URL must be at most 255 characters")
    private String image;

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
}
