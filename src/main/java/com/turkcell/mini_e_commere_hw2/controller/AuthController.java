package com.turkcell.mini_e_commere_hw2.controller;

import an.awesome.pipelinr.Pipeline;
import com.turkcell.mini_e_commere_hw2.application.auth.command.create.CreateAuthAdminCommand;
import com.turkcell.mini_e_commere_hw2.application.auth.command.create.CreateAuthCustomerCommand;
import com.turkcell.mini_e_commere_hw2.application.auth.command.create.CreateAuthSellerCommand;
import com.turkcell.mini_e_commere_hw2.application.auth.command.create.AuthResponse;
import com.turkcell.mini_e_commere_hw2.application.auth.command.read.LoginCommand;
import com.turkcell.mini_e_commere_hw2.core.web.BaseController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController {

    public AuthController(Pipeline pipeline) {
        super(pipeline);
    }

    @PostMapping("register/admin")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AuthResponse registerAdmin(@RequestBody @Valid CreateAuthAdminCommand createAuthAdminCommand) {
        return createAuthAdminCommand.execute(pipeline);
    }

    @PostMapping("register/customer")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AuthResponse registerCustomer(@RequestBody @Valid CreateAuthCustomerCommand createAuthCustomerCommand) {
        return createAuthCustomerCommand.execute(pipeline);
    }

    @PostMapping("register/seller")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AuthResponse registerSeller(@RequestBody @Valid CreateAuthSellerCommand createAuthSellerCommand) {
        return createAuthSellerCommand.execute(pipeline);
    }

    @PostMapping("login")
    @ResponseStatus(code = HttpStatus.OK)
    public AuthResponse login(@RequestBody @Valid LoginCommand loginCommand) {
        return loginCommand.execute(pipeline);
    }
}