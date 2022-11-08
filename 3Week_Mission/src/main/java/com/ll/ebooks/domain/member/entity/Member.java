package com.ll.ebooks.domain.member.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import com.ll.ebooks.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts;

    private int restCash = 0;

    @Builder
    public Member(String username, String password, String nickname, String email, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    public void modify(String email, String nickname, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public void modifyPassword(String password) {
        this.password = password;
    }

    public void modifyCash(int newCash) {
        this.restCash = newCash;
    }
}
