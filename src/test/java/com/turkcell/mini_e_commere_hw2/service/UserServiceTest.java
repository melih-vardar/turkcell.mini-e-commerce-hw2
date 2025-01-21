package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.user.AuthUserDto;
import com.turkcell.mini_e_commere_hw2.dto.user.LoginDto;
import com.turkcell.mini_e_commere_hw2.dto.user.RegisterDto;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserBusinessRules userBusinessRules;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userBusinessRules, jwtService, passwordEncoder);
    }

    @Test
    void register_WithValidData_ShouldSucceed() {
        // Arrange
        RegisterDto registerDto = new RegisterDto("John", "Doe", "johndoe@example.com", "Test123!");

        String mockToken = "mock.jwt.token";
        when(jwtService.generateToken(any())).thenReturn(mockToken);

        // Act
        AuthUserDto result = userService.add(registerDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockToken, result.getToken());
        verify(userBusinessRules).usernameMustNotExist(registerDto.getUsername());
        verify(userBusinessRules).passwordMustBeValid(registerDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_WithExistingUsername_ShouldThrowException() {
        // Arrange
        RegisterDto registerDto = new RegisterDto(null, null, "existing@example.com", "Test123!");

        doThrow(new BusinessException("Username already exist"))
            .when(userBusinessRules).usernameMustNotExist(registerDto.getUsername());

        // Act & Assert
        assertThrows(BusinessException.class, () -> userService.add(registerDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        RegisterDto registerDto = new RegisterDto(null, null, "test@example.com", "weak");

        doThrow(new BusinessException("Password must be at least 8 characters"))
            .when(userBusinessRules).passwordMustBeValid(registerDto.getPassword());

        // Act & Assert
        assertThrows(BusinessException.class, () -> userService.add(registerDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_WithValidCredentials_ShouldSucceed() {
        // Arrange
        LoginDto loginDto = new LoginDto("test@example.com", "Test123!");

        User mockUser = new User();
        mockUser.setUsername(loginDto.getUsername());
        mockUser.setPassword("hashedPassword");

        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(loginDto.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(loginDto.getUsername())).thenReturn("mock.jwt.token");

        // Act
        AuthUserDto result = userService.login(loginDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        verify(userRepository).findByUsername(loginDto.getUsername());
        verify(passwordEncoder).matches(loginDto.getPassword(), mockUser.getPassword());
    }

    @Test
    void login_WithInvalidUsername_ShouldThrowException() {
        // Arrange
        LoginDto loginDto = new LoginDto("nonexistent@example.com", "Test123!");

        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> userService.login(loginDto));
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        LoginDto loginDto = new LoginDto("test@example.com", "WrongPassword123!");

        User mockUser = new User();
        mockUser.setUsername(loginDto.getUsername());
        mockUser.setPassword(new BCryptPasswordEncoder().encode("Test123!")); // Different password

        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThrows(BusinessException.class, () -> userService.login(loginDto));
    }

    @Test
    void findById_WithValidId_ShouldReturnUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> userService.findById(userId));
    }
} 