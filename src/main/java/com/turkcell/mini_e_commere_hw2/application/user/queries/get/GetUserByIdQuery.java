package com.turkcell.mini_e_commere_hw2.application.user.queries.get;

import an.awesome.pipelinr.Command;
import com.turkcell.mini_e_commere_hw2.dto.user.UserListingDto;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.UserRepository;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetUserByIdQuery implements Command<UserListingDto> {
    @NotNull(message = "ID cannot be null")
    private UUID id;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<GetUserByIdQuery, UserListingDto> {
        private final UserRepository userRepository;

        @Override
        public UserListingDto handle(GetUserByIdQuery query) {
            User user = userRepository.findById(query.getId())
                .orElseThrow(() -> new BusinessException("User not found"));

            return mapToDto(user);
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