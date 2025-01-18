package com.turkcell.mini_e_commere_hw2.service;
import com.turkcell.mini_e_commere_hw2.dto.user.LoginDto;
import com.turkcell.mini_e_commere_hw2.dto.user.RegisterDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        bCryptPasswordEncoder = new BCryptPasswordEncoder(); // TODO: Bean olarak ekle.
    }

    @Override
    public void add(RegisterDto registerDto) {
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
    }

    @Override
    public String login(LoginDto loginDto) {
        User dbUser = userRepository
                .findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BusinessException("Invalid or wrong credentials."));

        boolean isPasswordCorrect = bCryptPasswordEncoder
                .matches(loginDto.getPassword(), dbUser.getPassword());

        if(!isPasswordCorrect)
            throw new BusinessException("Invalid or wrong credentials.");

        return this.jwtService.generateToken(dbUser.getUsername());
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));
    }
}
