package com.ll.ebooks.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    MEMBER(3),
    WRITER(4),
    ADMIN(7);

    private final Integer auth_level;

}
