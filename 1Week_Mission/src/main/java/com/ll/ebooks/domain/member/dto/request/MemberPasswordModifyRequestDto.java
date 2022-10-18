package com.ll.ebooks.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class MemberPasswordModifyRequestDto {

    @NotBlank(message = "비밀번호를 변경하기 위해 기존 비밀번호를 입력해주세요.")
    private String oldPassword;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,16}", message = "영문자와 숫자, 특수기호가 1개 이상 포함된 6~16자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 위해 같은 비밀번호를 입력해 주세요.")
    private String passwordCheck;

    public void encodingPassword(String password) {
        this.password = password;
    }
}
