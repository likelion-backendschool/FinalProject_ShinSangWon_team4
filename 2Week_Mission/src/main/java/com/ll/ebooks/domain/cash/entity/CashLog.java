package com.ll.ebooks.domain.cash.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class CashLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int price;

    private String eventType;

    @Builder
    public CashLog(Member member, int price, String eventType) {
        this.member = member;
        this.price = price;
        this.eventType = eventType;
    }
}
