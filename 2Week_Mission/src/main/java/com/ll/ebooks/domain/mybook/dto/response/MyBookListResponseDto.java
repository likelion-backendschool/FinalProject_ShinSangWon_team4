package com.ll.ebooks.domain.mybook.dto.response;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.mybook.entity.MyBook;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyBookListResponseDto {

    private Long productId;
    private String subject;
    private int price;
    private Member member;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public MyBookListResponseDto(MyBook entity) {
        this.productId = entity.getProduct().getId();
        this.subject = entity.getProduct().getSubject();
        this.price = entity.getProduct().getPrice();
        this.member = entity.getProduct().getMember();
        this.createdDate = entity.getProduct().getCreatedDate();
        this.modifiedDate = entity.getProduct().getModifiedDate();
    }
}
