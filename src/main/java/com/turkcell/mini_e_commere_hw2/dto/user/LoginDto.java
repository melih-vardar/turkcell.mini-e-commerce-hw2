package com.turkcell.mini_e_commere_hw2.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank(message = "Username cannot be empty")
    @Email
    private String username;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
