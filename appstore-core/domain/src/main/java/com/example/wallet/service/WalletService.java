package com.example.wallet.service;

import com.example.wallet.model.entity.Wallet;
import com.example.wallet.model.request.CreateWalletRequest;
import com.example.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public void createWallet(CreateWalletRequest createWalletRequest) {
        Wallet wallet = CreateWalletRequest.to(createWalletRequest);
    }
}
