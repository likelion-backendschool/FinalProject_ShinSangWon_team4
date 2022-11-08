package com.ll.ebooks.domain.product.dto.response;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponseDto {

    private Long id;
    private String subject;
    private int price;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private Member member;

    private PostKeyword postKeyword;
    public ProductResponseDto(Product entity) {
        this.id = entity.getId();
        this.subject = entity.getSubject();
        this.price = entity.getPrice();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.member = entity.getMember();
        this.postKeyword = entity.getPostKeyword();
    }
}
