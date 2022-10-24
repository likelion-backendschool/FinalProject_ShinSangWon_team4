package com.ll.ebooks.domain.post.dto.response;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.posttag.entity.PostTag;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {

    private Long id;
    private String subject;
    private String content;
    private String contentHtml;
    private Member member;

    private List<PostTag> hashTags;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.subject = entity.getSubject();
        this.content = entity.getContent();
        this.contentHtml = entity.getContentHtml();
        this.member = entity.getMember();
        this.hashTags = entity.getHashTags();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

    public String getHashTagToString() {

        if(hashTags.isEmpty()) {
            return "";
        }

        return "#" + hashTags
                .stream()
                .map(hashTag -> hashTag.getPostKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" #"))
                .trim();
    }
}
