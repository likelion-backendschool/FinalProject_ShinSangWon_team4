package com.ll.ebooks.domain.posttag.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class PostTag extends BaseEntity {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne
    private Member member;

    @ManyToOne
    private PostKeyword postKeyword;
}
