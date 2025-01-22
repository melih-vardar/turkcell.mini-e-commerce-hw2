package com.turkcell.mini_e_commere_hw2.service.application;


import com.turkcell.mini_e_commere_hw2.dto.user.*;

public interface AuthService {
    AuthUserDto register(RegisterAdminDto registerAdminDto);
    AuthUserDto register(RegisterCustomerDto registerCustomerDto);
    AuthUserDto register(RegisterSellerDto registerSellerDto);
    AuthUserDto login(LoginDto loginDto);
}
