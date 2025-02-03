package com.turkcell.mini_e_commere_hw2.controller;

import an.awesome.pipelinr.Pipeline;
import com.turkcell.mini_e_commere_hw2.application.category.commands.create.CreateCategoryCommand;
import com.turkcell.mini_e_commere_hw2.application.category.commands.create.CreatedCategoryResponse;
import com.turkcell.mini_e_commere_hw2.application.category.commands.delete.DeleteCategoryCommand;
import com.turkcell.mini_e_commere_hw2.application.category.commands.update.UpdateCategoryCommand;
import com.turkcell.mini_e_commere_hw2.application.category.queries.get.GetCategoryByIdQuery;
import com.turkcell.mini_e_commere_hw2.application.category.queries.list.GetAllCategoriesQuery;
import com.turkcell.mini_e_commere_hw2.application.category.queries.list.GetCategoryTreeQuery;
import com.turkcell.mini_e_commere_hw2.application.category.queries.list.GetSubCategoriesQuery;
import com.turkcell.mini_e_commere_hw2.core.web.BaseController;
import com.turkcell.mini_e_commere_hw2.dto.category.CategoryListingDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController {

    public CategoryController(Pipeline pipeline) {
        super(pipeline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedCategoryResponse add(@RequestBody @Valid CreateCategoryCommand command) {
        return pipeline.send(command);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody @Valid UpdateCategoryCommand command) {
        command.setId(id);
        pipeline.send(command);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        pipeline.send(new DeleteCategoryCommand(id));
    }

    @GetMapping
    public List<CategoryListingDto> getAll() {
        return pipeline.send(new GetAllCategoriesQuery());
    }

    @GetMapping("/{id}")
    public CategoryListingDto getById(@PathVariable Integer id) {
        return pipeline.send(new GetCategoryByIdQuery(id));
    }

    @PostMapping("/{parentId}/subcategories")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedCategoryResponse addSubCategory(
            @PathVariable Integer parentId,
            @RequestBody @Valid CreateCategoryCommand command) {
        command.setParentId(parentId);
        return pipeline.send(command);
    }

    @GetMapping("/{parentId}/subcategories")
    public List<CategoryListingDto> getAllSubCategories(@PathVariable Integer parentId) {
        return pipeline.send(new GetSubCategoriesQuery(parentId));
    }

    @GetMapping("/tree")
    public List<CategoryListingDto> getCategoryTree() {
        return pipeline.send(new GetCategoryTreeQuery());
    }
}
