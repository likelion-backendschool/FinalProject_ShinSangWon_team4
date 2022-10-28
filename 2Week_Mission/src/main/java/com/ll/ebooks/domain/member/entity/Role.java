package com.ll.ebooks.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    MEMBER(3, "일반 회원"),
    WRITER(4, "작가"),
    ADMIN(7, "관리자");

    private final Integer auth_level;
    private final String description;

}
