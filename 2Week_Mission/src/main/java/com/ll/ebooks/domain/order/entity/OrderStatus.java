package com.ll.ebooks.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    READY("결제 대기"), PAID("결제 완료"), CANCELED("주문 취소"), REFUNDED("환불"), FAILED("주문 실패");

    private final String description;
}
