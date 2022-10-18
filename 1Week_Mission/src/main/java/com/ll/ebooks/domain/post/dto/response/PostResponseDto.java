package com.ll.ebooks.domain.post.dto.response;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private String subject;
    private String content;
    private Member member;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.subject = entity.getSubject();
        this.content = entity.getContent();
        this.member = entity.getMember();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
