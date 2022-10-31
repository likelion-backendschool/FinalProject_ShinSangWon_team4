package com.ll.ebooks.domain.product.dto.response;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductListResponseDto {

    private Long id;
    private String subject;
    private Member member;
    private int price;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public ProductListResponseDto(Product entity) {
        this.id = entity.getId();
        this.subject = entity.getSubject();
        this.member = entity.getMember();
        this.price = entity.getPrice();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
