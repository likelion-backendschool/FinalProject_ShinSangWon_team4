package com.ll.ebooks.domain.post.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseEntity {

    @Column(length = 200, nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private String contentHtml;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Post(String subject, String content, String contentHtml, Member member) {
        this.subject = subject;
        this.content = content;
        this.contentHtml = contentHtml;
        this.member = member;
    }

    public void modify(String subject, String content, String contentHtml) {
        this.subject = subject;
        this.content = content;
        this.contentHtml = contentHtml;
    }

}
