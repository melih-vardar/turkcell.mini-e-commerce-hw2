package com.turkcell.mini_e_commere_hw2.application.category.commands.delete;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.repository.CategoryRepository;
import com.turkcell.mini_e_commere_hw2.rules.CategoryBusinessRules;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
public class DeleteCategoryCommand implements Command<Void> {
    @NotNull(message = "Category ID cannot be null")
    private Integer id;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<DeleteCategoryCommand, Void> {
        private final CategoryRepository categoryRepository;
        private final CategoryBusinessRules categoryBusinessRules;


        @Override
        public Void handle(DeleteCategoryCommand command) {
            categoryBusinessRules.categoryMustExist(command.getId());
            categoryBusinessRules.categoryMustNotHaveAssociatedProducts(command.getId());
            categoryBusinessRules.categoryMustNotHaveSubCategories(command.getId());

            categoryRepository.deleteById(command.getId());
            return null;
        }
    }
}
