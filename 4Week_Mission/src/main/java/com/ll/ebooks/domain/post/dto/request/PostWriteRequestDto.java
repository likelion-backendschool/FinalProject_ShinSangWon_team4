package com.ll.ebooks.domain.post.dto.request;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class PostWriteRequestDto {

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    @Size(max = 200)
    private String subject;
    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String content;

    private String hashTags;
    private String contentHtml;

    @Builder
    public PostWriteRequestDto(String subject, String content, String hashTags) {
        this.subject = subject;
        this.content = content;
        this.hashTags = hashTags;
    }

    public Post toEntity(Member member) {
        return Post.builder()
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .member(member)
                .build();
    }
}
