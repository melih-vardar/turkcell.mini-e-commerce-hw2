package com.turkcell.mini_e_commere_hw2.service.application;

import com.turkcell.mini_e_commere_hw2.dto.user.*;
import com.turkcell.mini_e_commere_hw2.entity.*;
import com.turkcell.mini_e_commere_hw2.rules.OperationClaimBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.UserBusinessRules;
import com.turkcell.mini_e_commere_hw2.service.domain.*;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import com.turkcell.mini_e_commere_hw2.util.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final UserBusinessRules userBusinessRules;
    private final OperationClaimBusinessRules operationClaimBusinessRules;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final OperationClaimService operationClaimService;
    private final AdminService adminService;
    private final CustomerService customerService;
    private final SellerService sellerService;


    @Override
    public AuthUserDto register(RegisterAdminDto registerAdminDto) {
        userBusinessRules.usernameMustNotExist(registerAdminDto.getUsername());
        userBusinessRules.passwordMustBeValid(registerAdminDto.getPassword());

        Admin admin = new Admin();
        admin.setName(registerAdminDto.getName());
        admin.setSurname(registerAdminDto.getSurname());
        admin.setUsername(registerAdminDto.getUsername());
        admin.setPassword(passwordEncoder.encode(registerAdminDto.getPassword()));

        // Set admin role
        addIfNotExistsOperationClaim("admin");

        List<OperationClaim> claims = new ArrayList<>();
        claims.add(operationClaimService.getOperationClaimByName("admin"));
        admin.setOperationClaims(claims);

        adminService.create(admin);

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(admin.getUsername(), getRoles(admin)));

        return authUserDto;
    }

    @Override
    public AuthUserDto register(RegisterCustomerDto registerCustomerDto) {
        userBusinessRules.usernameMustNotExist(registerCustomerDto.getUsername());
        userBusinessRules.passwordMustBeValid(registerCustomerDto.getPassword());

        Customer customer = new Customer();
        customer.setName(registerCustomerDto.getName());
        customer.setSurname(registerCustomerDto.getSurname());
        customer.setUsername(registerCustomerDto.getUsername());
        customer.setPassword(passwordEncoder.encode(registerCustomerDto.getPassword()));

        // Create and associate cart
        Cart cart = new Cart();
        cart.setUser(customer);
        cart.setTotalPrice(BigDecimal.ZERO);
        customer.setCart(cart);

        // Set customer role
        addIfNotExistsOperationClaim("customer");

        List<OperationClaim> claims = new ArrayList<>();
        claims.add(operationClaimService.getOperationClaimByName("customer"));
        customer.setOperationClaims(claims);

        customerService.create(customer);

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(customer.getUsername(), getRoles(customer)));

        return authUserDto;
    }

    @Override
    public AuthUserDto register(RegisterSellerDto registerSellerDto) {
        userBusinessRules.usernameMustNotExist(registerSellerDto.getUsername());
        userBusinessRules.passwordMustBeValid(registerSellerDto.getPassword());

        Seller seller = new Seller();
        seller.setName(registerSellerDto.getName());
        seller.setSurname(registerSellerDto.getSurname());
        seller.setUsername(registerSellerDto.getUsername());
        seller.setPassword(passwordEncoder.encode(registerSellerDto.getPassword()));
        seller.setCompanyName(registerSellerDto.getCompanyName());

        // Set seller role
        addIfNotExistsOperationClaim("seller");

        List<OperationClaim> claims = new ArrayList<>();
        claims.add(operationClaimService.getOperationClaimByName("seller"));
        seller.setOperationClaims(claims);

        sellerService.create(seller);

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(seller.getUsername(), getRoles(seller)));

        return authUserDto;
    }

    @Override
    public AuthUserDto login(LoginDto loginDto) {
        User dbUser = userService.getByUsername(loginDto.getUsername());

        boolean isPasswordCorrect = passwordEncoder
                .matches(loginDto.getPassword(), dbUser.getPassword());

        if(!isPasswordCorrect)
            throw new BusinessException("Invalid or wrong credentials.");

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(dbUser.getUsername(), getRoles(dbUser)));
        return authUserDto;
    }

    private void addIfNotExistsOperationClaim(String name){
        try{
            operationClaimBusinessRules.operationClaimMustExist(name);
        } catch (BusinessException e){
            operationClaimService.addOperationClaim(name);
        }
    }

    private Map<String,Object> getRoles(User user){
        Map<String,Object> roles = new HashMap<>();
        roles.put("roles", user.getOperationClaims().stream().map(c->c.getName()).toList());
        return roles;
    }
}
