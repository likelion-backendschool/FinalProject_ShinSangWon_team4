package com.ll.ebooks.domain.product.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostKeyword postKeyword;

    private String subject;

    private int price;

    @Builder
    public Product(Member member, PostKeyword postKeyword, String subject, int price) {
        this.member = member;
        this.postKeyword = postKeyword;
        this.subject = subject;
        this.price = price;
    }

    public void modify(String subject, int price) {
        this.subject = subject;
        this.price = price;
    }

    public int getSalePrice() {
        return getPrice();
    }

    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.5);
    }

    public boolean isOrderable() {
        return true;
    }
}
