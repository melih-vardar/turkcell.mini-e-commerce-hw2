package com.turkcell.mini_e_commere_hw2.controller;

import an.awesome.pipelinr.Pipeline;
import com.turkcell.mini_e_commere_hw2.application.user.commands.delete.DeleteUserCommand;
import com.turkcell.mini_e_commere_hw2.application.user.commands.update.UpdateUserCommand;
import com.turkcell.mini_e_commere_hw2.application.user.queries.get.GetUserByIdQuery;
import com.turkcell.mini_e_commere_hw2.application.user.queries.list.GetAllUsersQuery;
import com.turkcell.mini_e_commere_hw2.core.web.BaseController;
import com.turkcell.mini_e_commere_hw2.dto.user.UserListingDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController extends BaseController {
    
    public UsersController(Pipeline pipeline) {
        super(pipeline);
    }

    @GetMapping
    public List<UserListingDto> getAll() {
        return pipeline.send(new GetAllUsersQuery());
    }

    @GetMapping("/{id}")
    public UserListingDto getById(@PathVariable UUID id) {
        return pipeline.send(new GetUserByIdQuery(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        pipeline.send(new DeleteUserCommand(id));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable UUID id, @RequestBody @Valid UpdateUserCommand command) {
        command.setId(id);
        pipeline.send(command);
    }
}
