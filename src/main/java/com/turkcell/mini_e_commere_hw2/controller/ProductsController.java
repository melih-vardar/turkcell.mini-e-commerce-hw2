package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.product.CreateProductDto;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;
import com.turkcell.mini_e_commere_hw2.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController()
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public void add(@RequestBody @Valid CreateProductDto createProductDto) {
        this.productService.add(createProductDto);
    }

    @GetMapping
    public List<ProductListingDto> getAll() {
        return this.productService.getAll();
    }

    @GetMapping("/search")
    public List<ProductListingDto> search(@RequestParam String categoryId, @RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice, @RequestParam boolean inStock) {
        return this.productService.search(categoryId, minPrice, maxPrice, inStock);
    }

    @PutMapping
    public void update(@RequestBody UpdateProductDto updateProductDto) {
        this.productService.update(updateProductDto);
    }
}
