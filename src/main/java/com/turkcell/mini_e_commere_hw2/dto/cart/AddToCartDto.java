package com.turkcell.mini_e_commere_hw2.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartDto {
    @NotNull(message = "Cart ID cannot be null")
    private Integer cartId;
    @NotNull(message = "Product ID cannot be null")
    private Integer productId;
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}

