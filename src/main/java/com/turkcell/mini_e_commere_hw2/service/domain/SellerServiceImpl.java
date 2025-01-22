package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Seller;
import com.turkcell.mini_e_commere_hw2.repository.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements SellerService{
    private final SellerRepository sellerRepository;
    @Override
    public Seller create(Seller seller) {
        return sellerRepository.save(seller);
    }
}