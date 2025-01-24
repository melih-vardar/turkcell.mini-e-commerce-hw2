package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.user.UserListingDto;
import com.turkcell.mini_e_commere_hw2.dto.user.UserUpdateDto;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    @Override
    public List<UserListingDto> getAll() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserListingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserListingDto getById(UUID id) {
        return modelMapper.map(userService.getById(id), UserListingDto.class);
    }

    @Override
    public void delete(UUID id) {
        userService.delete(id);
    }

    @Override
    public void update(UUID id, UserUpdateDto userUpdateDto) {
        User user = modelMapper.map(userUpdateDto, User.class);
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(user);
    }
}
