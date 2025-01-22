package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.user.*;
import com.turkcell.mini_e_commere_hw2.service.application.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("register/customer")
    public AuthUserDto register(@RequestBody @Valid RegisterCustomerDto registerCustomerDto) {
        return authService.register(registerCustomerDto);
    }

    @PostMapping("register/admin")
    public AuthUserDto registerAdmin(@RequestBody @Valid RegisterAdminDto registerAdminDto) {
        return authService.register(registerAdminDto);
    }

    @PostMapping("register/seller")
    public AuthUserDto registerSeller(@RequestBody @Valid RegisterSellerDto registerSellerDto) {
        return authService.register(registerSellerDto);
    }

    @PostMapping("login")
    public AuthUserDto login(@RequestBody @Valid LoginDto loginDto) {
        return authService.login(loginDto);
    }
}