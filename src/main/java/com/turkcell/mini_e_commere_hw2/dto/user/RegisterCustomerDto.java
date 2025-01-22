package com.turkcell.mini_e_commere_hw2.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCustomerDto extends RegisterDto{
    @NotNull(message = "Address cannot be null")
    private String address;
}
