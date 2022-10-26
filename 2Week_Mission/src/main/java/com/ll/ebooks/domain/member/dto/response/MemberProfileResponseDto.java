package com.ll.ebooks.domain.member.dto.response;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.entity.Role;
import lombok.Getter;

@Getter
public class MemberProfileResponseDto {

    private String username;
    private String nickname;
    private String email;
    private Role role;
    private int restCash;

    public MemberProfileResponseDto(Member entity) {
        this.username = entity.getUsername();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.role = entity.getRole();
        this.restCash = entity.getRestCash();
    }
}
