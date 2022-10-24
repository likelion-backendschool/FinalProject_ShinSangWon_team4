package com.ll.ebooks.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class LoginRequestDto {

    @Setter
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
}
