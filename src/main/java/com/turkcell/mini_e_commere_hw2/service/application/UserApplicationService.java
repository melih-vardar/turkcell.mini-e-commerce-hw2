package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.user.UserListingDto;

import java.util.List;
import java.util.UUID;

public interface UserApplicationService {

    List<UserListingDto> getAll();

    UserListingDto getById(UUID id);
}
