package com.turkcell.mini_e_commere_hw2.service;
import com.turkcell.mini_e_commere_hw2.dto.user.AuthUserDto;
import com.turkcell.mini_e_commere_hw2.dto.user.LoginDto;
import com.turkcell.mini_e_commere_hw2.dto.user.RegisterDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserBusinessRules userBusinessRules;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, UserBusinessRules userBusinessRules, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userBusinessRules = userBusinessRules;
        this.jwtService = jwtService;
        bCryptPasswordEncoder = new BCryptPasswordEncoder(); // TODO: Bean olarak ekle.
    }

    @Override
    public AuthUserDto add(RegisterDto registerDto) {
        userBusinessRules.usernameMustNotExist(registerDto.getUsername());
        userBusinessRules.passwordMustBeValid(registerDto.getPassword());

        User user = new User();
        user.setName(registerDto.getName());
        user.setSurname(registerDto.getSurname());
        user.setUsername(registerDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));

        // Create and associate cart
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        user.setCart(cart);

        userRepository.save(user);

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(user.getUsername()));

        return authUserDto;
    }

    @Override
    public AuthUserDto login(LoginDto loginDto) {
        User dbUser = userRepository
                .findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BusinessException("Invalid or wrong credentials."));

        boolean isPasswordCorrect = bCryptPasswordEncoder
                .matches(loginDto.getPassword(), dbUser.getPassword());

        if(!isPasswordCorrect)
            throw new BusinessException("Invalid or wrong credentials.");

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(dbUser.getUsername()));
        return authUserDto;
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));
    }
}
