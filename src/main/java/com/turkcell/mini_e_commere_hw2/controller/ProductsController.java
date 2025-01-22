package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;
import com.turkcell.mini_e_commere_hw2.service.application.ProductApplicationService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController()
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductsController {
    private final ProductApplicationService productApplicationService;

    @PostMapping
    public void add(@RequestBody @Valid CreateProductDto createProductDto) {
        this.productApplicationService.add(createProductDto);
    }

    @GetMapping
    public List<ProductListingDto> getAll() {
        return this.productApplicationService.getAll();
    }

    @GetMapping("/search")
    public List<ProductListingDto> search(@RequestParam @Nullable String categoryId,
                                          @RequestParam @Nullable String subCategoryId,
                                          @RequestParam @Nullable BigDecimal minPrice,
                                          @RequestParam @Nullable BigDecimal maxPrice,
                                          @RequestParam @Nullable Boolean inStock) {
        return this.productApplicationService.search(categoryId, subCategoryId, minPrice, maxPrice, inStock);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateProductDto updateProductDto) {
        this.productApplicationService.update(updateProductDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        this.productApplicationService.delete(id);
    }
}
