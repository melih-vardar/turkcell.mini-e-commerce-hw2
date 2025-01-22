package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.user.UserListingDto;
import com.turkcell.mini_e_commere_hw2.service.application.UserApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UsersController {
    private final UserApplicationService userApplicationService;

    @GetMapping
    public List<UserListingDto> getAll() {
        return this.userApplicationService.getAll();
    }

    @GetMapping("/{id}")
    public UserListingDto getById(@PathVariable UUID id) {
        return this.userApplicationService.getById(id);
    }
}
