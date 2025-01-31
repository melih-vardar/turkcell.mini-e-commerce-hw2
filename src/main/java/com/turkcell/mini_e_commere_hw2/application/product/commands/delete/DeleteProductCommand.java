package com.turkcell.mini_e_commere_hw2.application.product.commands.delete;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.rules.ProductBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
public class DeleteProductCommand implements Command<Void>
{
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @Component
    public static class Handler implements Command.Handler<DeleteProductCommand, Void>
    {
        private final ProductRepository productRepository;
        private final ProductBusinessRules productBusinessRules;

        public Handler(ProductRepository productRepository, ProductBusinessRules productBusinessRules) {
            this.productRepository = productRepository;
            this.productBusinessRules = productBusinessRules;
        }

        @Override
        public Void handle(DeleteProductCommand command)
        {
            productBusinessRules.productIdMustExist(command.getId());
            productBusinessRules.productMustNotBeAssociatedWithAnyOrder(command.getId());

            Product product = productRepository.findById(command.getId())
                    .orElseThrow(() -> new BusinessException("Product not found"));

            productRepository.delete(product);
            return null;
        }
    }
}
