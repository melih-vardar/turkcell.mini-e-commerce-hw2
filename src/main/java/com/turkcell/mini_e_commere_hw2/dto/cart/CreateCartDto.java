package com.turkcell.mini_e_commere_hw2.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartDto {
    @NotNull(message = "User ID cannot be null")
    private UUID userId;
}
