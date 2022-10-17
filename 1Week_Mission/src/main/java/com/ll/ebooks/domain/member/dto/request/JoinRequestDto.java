package com.ll.ebooks.domain.member.dto.request;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequestDto {

    @NotBlank(message = "아이디는 필수 입력 사항입니다.")
    @Size(min = 4, max = 16, message = "아이디는 4~16자리여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,16}",
            message = "영문자와 숫자, 특수기호가 1개 이상 포함된 6~16자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 위해 같은 비밀번호를 입력해 주세요.")
    private String passwordCheck;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,8}$", message = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "이메일 형식을 올바르게 입력해주세요.")
    private String email;

    @Builder
    public JoinRequestDto(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Member toEntity(Role role) {
        return Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .role(role)
                .build();

    }

    public void encodingPassword(String password) {
        this.password = password;
    }
}
