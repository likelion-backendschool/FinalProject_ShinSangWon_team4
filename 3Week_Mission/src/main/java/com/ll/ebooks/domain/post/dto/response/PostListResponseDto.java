package com.ll.ebooks.domain.post.dto.response;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {

    private Long id;
    private String subject;
    private Member member;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity) {
        this.id = entity.getId();
        this.subject = entity.getSubject();
        this.member = entity.getMember();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
