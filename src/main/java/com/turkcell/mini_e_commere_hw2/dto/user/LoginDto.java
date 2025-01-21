package com.turkcell.mini_e_commere_hw2.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank(message = "Username cannot be empty")
    @Email(message = "Invalid email format")
    private String username;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
