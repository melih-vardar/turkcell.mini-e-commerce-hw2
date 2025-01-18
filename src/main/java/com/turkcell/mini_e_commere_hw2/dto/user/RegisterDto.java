package com.turkcell.mini_e_commere_hw2.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @NotBlank(message = "Username cannot be empty")
    @Email
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
