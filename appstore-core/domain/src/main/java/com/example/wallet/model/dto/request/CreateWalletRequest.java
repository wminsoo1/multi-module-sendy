package com.example.wallet.model.dto.request;

import com.example.wallet.model.entity.Wallet;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateWalletRequest {

    @NotNull(message = "유저id는 필수 항목입니다.")
    private Long memberId;

    public static Wallet toWallet(CreateWalletRequest createWalletRequest) {
        return Wallet.builder()
                .id(createWalletRequest.getMemberId())
                .build();
    }
}
