package com.ll.ebooks.domain.order.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "product_order")
@Entity
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime payDate;

    private String name;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태 : READY, ORDER, CANCELED, REFUNDED

    @Builder
    public Order(Member member) {
        this.member = member;
        this.orderStatus = OrderStatus.READY;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItem.mapToOrder(this);
        orderItems.add(orderItem);
    }

    public int getTotalPayPrice() {
        int payPrice = 0;

        for(OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPrice();
        }

        return payPrice;
    }

    public void setPaymentDone() {
        for(OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }

        this.orderStatus = OrderStatus.PAID;
        this.payDate = LocalDateTime.now();
    }

    public void setRefundDone() {
        for(OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }

        this.orderStatus = OrderStatus.REFUNDED;
    }

    public void setCancel() {
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if(orderItems.size() > 1) {
            name += " 외 %d권".formatted(orderItems.size() - 1);
        }

        this.name = name;

    }
}
