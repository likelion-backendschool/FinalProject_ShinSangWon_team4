package com.ll.ebooks.domain.cartitem.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Product product;

    @Builder
    public CartItem(Member member, Product product) {
        this.member = member;
        this.product = product;
    }
}
