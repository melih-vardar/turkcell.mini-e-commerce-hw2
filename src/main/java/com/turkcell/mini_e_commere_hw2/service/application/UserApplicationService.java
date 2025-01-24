package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.user.UserListingDto;
import com.turkcell.mini_e_commere_hw2.dto.user.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserApplicationService {

    List<UserListingDto> getAll();

    UserListingDto getById(UUID id);

    void delete(UUID id);

    void update(UUID id, UserUpdateDto userUpdateDto);
}
