package com.turkcell.mini_e_commere_hw2.service;


import com.turkcell.mini_e_commere_hw2.dto.user.AuthUserDto;
import com.turkcell.mini_e_commere_hw2.dto.user.LoginDto;
import com.turkcell.mini_e_commere_hw2.dto.user.RegisterDto;
import com.turkcell.mini_e_commere_hw2.entity.User;

import java.util.UUID;

public interface UserService {
  AuthUserDto add(RegisterDto registerDto);
  AuthUserDto login(LoginDto loginDto);
  User findById(UUID userId);
}
