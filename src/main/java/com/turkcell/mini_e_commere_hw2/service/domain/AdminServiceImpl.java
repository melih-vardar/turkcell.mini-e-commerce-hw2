package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Admin;
import com.turkcell.mini_e_commere_hw2.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    @Override
    public Admin create(Admin admin) {
        return adminRepository.save(admin);
    }
}
