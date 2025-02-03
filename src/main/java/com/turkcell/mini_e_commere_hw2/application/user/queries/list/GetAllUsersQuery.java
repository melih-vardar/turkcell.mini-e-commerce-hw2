package com.turkcell.mini_e_commere_hw2.application.user.queries.list;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.user.UserListingDto;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GetAllUsersQuery implements Command<List<UserListingDto>> {

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<GetAllUsersQuery, List<UserListingDto>> {
        private final UserRepository userRepository;

        @Override
        public List<UserListingDto> handle(GetAllUsersQuery query) {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        private UserListingDto mapToDto(User user) {
            UserListingDto dto = new UserListingDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setSurname(user.getSurname());
            dto.setUsername(user.getUsername());
            return dto;
        }
    }
} 