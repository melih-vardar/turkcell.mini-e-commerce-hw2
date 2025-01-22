package com.turkcell.mini_e_commere_hw2.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSellerDto extends RegisterDto{
    @NotNull(message = "Company name cannot be null")
    private String companyName;
}
