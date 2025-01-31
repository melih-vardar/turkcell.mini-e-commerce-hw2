package com.turkcell.mini_e_commere_hw2.controller;

import an.awesome.pipelinr.Pipeline;
import com.turkcell.mini_e_commere_hw2.application.product.commands.create.CreateProductCommand;
import com.turkcell.mini_e_commere_hw2.application.product.commands.create.CreatedProductResponse;
import com.turkcell.mini_e_commere_hw2.application.product.commands.delete.DeleteProductCommand;
import com.turkcell.mini_e_commere_hw2.application.product.commands.update.UpdateProductCommand;
import com.turkcell.mini_e_commere_hw2.application.product.queries.get.GetListProductQuery;
import com.turkcell.mini_e_commere_hw2.application.product.queries.search.SearchProductsQuery;
import com.turkcell.mini_e_commere_hw2.core.web.BaseController;
import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController {
    
    public ProductController(Pipeline pipeline) {
        super(pipeline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedProductResponse createProduct(@RequestBody @Valid CreateProductCommand command) {
        return pipeline.send(command);
    }

    @GetMapping
    public List<ProductListingDto> getAll() {
        return pipeline.send(new GetListProductQuery());
    }

    @GetMapping("/search")
    public List<ProductListingDto> search(
            @RequestParam @Nullable String categoryId,
            @RequestParam @Nullable BigDecimal minPrice,
            @RequestParam @Nullable BigDecimal maxPrice,
            @RequestParam @Nullable Boolean inStock) {
        return pipeline.send(new SearchProductsQuery(categoryId, minPrice, maxPrice, inStock));
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateProductCommand command) {
        pipeline.send(command);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotNull Integer id) {
        pipeline.send(new DeleteProductCommand(id));
    }
} 