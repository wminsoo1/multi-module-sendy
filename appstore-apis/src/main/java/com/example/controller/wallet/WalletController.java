package com.example.controller.wallet;

import com.example.wallet.model.dto.request.ChargeRequest;
import com.example.wallet.model.dto.request.CreateWalletRequest;
import com.example.wallet.model.dto.response.PaymentResponse;
import com.example.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor()
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public void createWallet(@Valid @RequestBody CreateWalletRequest createWalletRequest) {
        walletService.createWallet(createWalletRequest);
    }

    @PostMapping("/charge")
    public BigDecimal charge(
            @RequestParam("walletId") Long walletId,
            @Valid @RequestBody ChargeRequest chargeRequest) {
        return walletService.charge(walletId, chargeRequest);
    }

    @PostMapping("/payment")
    public PaymentResponse payment(
            @RequestParam("walletId") Long walletId,
            @RequestParam("reservationNumber") String reservationNumber) {
        return walletService.payment(walletId, reservationNumber);
    }
}
