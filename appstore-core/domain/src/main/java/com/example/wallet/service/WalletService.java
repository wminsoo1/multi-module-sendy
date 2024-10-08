package com.example.wallet.service;

import com.example.delivery.exception.DeliveryException;
import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.entity.Delivery;
import com.example.delivery.model.entity.StopOver;
import com.example.delivery.repository.DeliveryRepository;
import com.example.delivery.repository.StopOverRepository;
import com.example.member.exception.MemberException;
import com.example.member.model.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.wallet.exception.WalletException;
import com.example.wallet.model.dto.response.PaymentResponse;
import com.example.wallet.model.entity.Wallet;
import com.example.wallet.model.dto.request.ChargeRequest;
import com.example.wallet.model.dto.request.CreateWalletRequest;
import com.example.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.example.delivery.exception.DeliveryErrorCode.*;
import static com.example.delivery.model.DeliveryStatus.*;
import static com.example.member.exception.MemberErrorCode.*;
import static com.example.wallet.exception.WalletErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {

    private static final int MAX_WALLET_COUNT = 3; //카드 개수 최대 3개

    private final WalletRepository walletRepository;
    private final DeliveryRepository deliveryRepository;
    private final StopOverRepository stopOverRepository;
    private final MemberRepository memberRepository;

    public void createWallet(CreateWalletRequest createWalletRequest) {
        Long memberId = createWalletRequest.getMemberId();
        validateWalletMaxCount(memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Wallet wallet = CreateWalletRequest.toWallet(createWalletRequest);
        wallet.updateMember(member);
        walletRepository.save(wallet);
    }

    public BigDecimal charge(Long walletId, ChargeRequest chargeRequest) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException(WALLET_NOT_FOUND));

        BigDecimal chargeBalance = chargeRequest.getChargeBalance();
        if (chargeBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException(NEGATIVE_OR_ZERO_CHARGE_AMOUNT);
        }

        wallet.updateBalance(wallet.getBalance().add(chargeBalance));

        return wallet.getBalance();
    }

    public PaymentResponse payment(Long walletId, String reservationNumber) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException(WALLET_NOT_FOUND));

        Delivery delivery = deliveryRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new DeliveryException(DELIVERY_NOT_FOUND));

        BigDecimal paymentBalance = delivery.getDeliveryFee();
        if (paymentBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException(NEGATIVE_OR_ZERO_PAYMENT_AMOUNT);
        }

        if (wallet.getBalance().compareTo(paymentBalance) < 0) {
            throw new WalletException(INSUFFICIENT_BALANCE);
        }

        wallet.updateBalance(wallet.getBalance().subtract(paymentBalance));
        delivery.updateDeliveryStatus(ASSIGNMENT_PENDING);

        List<StopOver> stopOverByReservationNumber = stopOverRepository.findStopOverByReservationNumber(reservationNumber);
        if (stopOverByReservationNumber == null || stopOverByReservationNumber.isEmpty()) {
            return PaymentResponse.from(paymentBalance, wallet, delivery, Collections.emptyList());
        }

        List<DeliveryAddress> deliveryAddresses = stopOverByReservationNumber.stream()
                .map(StopOver::getDeliveryAddress)
                .toList();
        return PaymentResponse.from(paymentBalance, wallet, delivery, deliveryAddresses);
    }

    private void validateWalletMaxCount(Long memberId) {
        if (walletRepository.findByMemberId(memberId).size() > MAX_WALLET_COUNT) {
            throw new WalletException(EXCEEDED_MAX_WALLET_COUNT);
        }
    }

}
