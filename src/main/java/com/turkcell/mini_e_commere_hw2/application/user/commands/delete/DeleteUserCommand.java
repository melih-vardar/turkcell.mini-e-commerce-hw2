package com.turkcell.mini_e_commere_hw2.application.user.commands.delete;

import an.awesome.pipelinr.Command;
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
public class DeleteUserCommand implements Command<Void> {
    @NotNull(message = "ID cannot be null")
    private UUID id;

    @Component
    @RequiredArgsConstructor
    public static class Handler implements Command.Handler<DeleteUserCommand, Void> {
        private final UserRepository userRepository;

        @Override
        public Void handle(DeleteUserCommand command) {
            if (!userRepository.existsById(command.getId())) {
                throw new BusinessException("User not found");
            }
            userRepository.deleteById(command.getId());
            return null;
        }
    }
} 