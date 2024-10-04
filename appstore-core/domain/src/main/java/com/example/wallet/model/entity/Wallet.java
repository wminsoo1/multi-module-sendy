package com.example.wallet.model.entity;

import com.example.member.model.entity.Member;
import com.example.wallet.model.CardCompany;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private CardCompany cardCompany;

    private BigDecimal balance;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateBalance(BigDecimal balance) {
        this.balance = balance;
    }

    private Wallet(Long id, Member member, CardCompany cardCompany, BigDecimal balance) {
        this.id = id;
        this.member = member;
        this.cardCompany = cardCompany;
        this.balance = balance;
    }
}
