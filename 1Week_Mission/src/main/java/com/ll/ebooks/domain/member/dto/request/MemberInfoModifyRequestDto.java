package com.ll.ebooks.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoModifyRequestDto {

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "이메일 형식을 올바르게 입력해주세요.")
    private String email;

    @Pattern(regexp = "[0-9a-zA-Z가-힣]{0,8}", message = "닉네임은 특수문자를 제외한 8자 이내여야 합니다.")
    private String nickname;

    @NotBlank(message = "회원 정보를 변경하기 위해 비밀번호를 입력해주세요.")
    private String passwordCheck;

    @Builder
    public MemberInfoModifyRequestDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
