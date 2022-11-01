package com.ll.ebooks.domain.rebate.entity;

import com.ll.ebooks.domain.cash.entity.CashLog;
import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.entity.OrderItem;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class RebateOrderItem extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    private String productName;
    private int price;
    private int salePrice;
    private int wholesalePrice;
    private int pgFee;
    private int payPrice;
    private int refundPrice;
    private boolean isPaid;
    private LocalDateTime payDate;

    // 구매자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member buyer;
    private String buyerName;

    // 정산 여부
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CashLog rebateCashLog;

    @Builder
    public RebateOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        order = orderItem.getOrder();
        product = orderItem.getProduct();
        productName = orderItem.getOrder().getName();
        price = orderItem.getPrice();
        salePrice = orderItem.getSalePrice();
        wholesalePrice = orderItem.getWholesalePrice();
        pgFee = orderItem.getPgFee();
        payPrice = orderItem.getPayPrice();
        refundPrice = orderItem.getRefundPrice();
        isPaid = orderItem.isPaid();
        payDate = orderItem.getPayDate();

        //추가 정보
        buyer = order.getMember();
        buyerName = order.getMember().getUsername();
    }

}
