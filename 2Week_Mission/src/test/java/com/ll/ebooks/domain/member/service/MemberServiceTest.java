package com.ll.ebooks.domain.member.service;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberInfoModifyRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberPasswordModifyRequestDto;
import com.ll.ebooks.domain.member.dto.request.UsernameFindRequestDto;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.entity.Role;
import com.ll.ebooks.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//tast data -> com.ll.ebooks.domain.global.initdata.InitDataBefore
@Transactional
@SpringBootTest
class MemberServiceTest {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceTest(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @BeforeEach
    void tearUp() {
/*        memberService.join(JoinRequestDto.builder()
                .username("dnjsml30")
                .password("test123!")
                .email("dnjsml30@naver.com")
                .nickname("상원")
                .build());*/
    }

    @Test
    @DisplayName("회원_가입된다")
    void test1() {
        //given


        //when
        List<Member> memberList = memberRepository.findAll();

        //then
        Member member = memberList.get(0);

        assertThat(member.getUsername()).isEqualTo("dnjsml30");

    }

    @Test
    @DisplayName("닉네임을_기입하지_않으면_멤버로_가입된다")
    void test2() {

        memberService.join(JoinRequestDto.builder()
                .username("test1234")
                .password("test123!")
                .email("test1234@naver.com")
                .nickname("")
                .build());

        //when
        List<Member> memberList = memberRepository.findAll();

        //then
        Member member = memberList.get(2);

        assertThat(member.getRole()).isEqualTo(Role.MEMBER);
    }

    @Test
    @DisplayName("비밀번호_암호화_수행된다")
    void test3() {
        //given

        //when
        List<Member> memberList = memberRepository.findAll();

        //then
        Member member = memberList.get(0);

        assertThat(member.getPassword()).isNotEqualTo("test123!");

    }

    @Test
    @DisplayName("회원정보_변경된다")
    void test4() {
        //given

        List<Member> memberList = memberRepository.findAll();
        Member member = memberList.get(0);
        //when
        memberService.modify(new MemberInfoModifyRequestDto("test123@test.com", "상투", "test123!"), member);
        //then
        assertThat(member.getEmail()).isEqualTo("test123@test.com");
        assertThat(member.getNickname()).isEqualTo("상투");


    }

    @Test
    @DisplayName("회원비밀번호_변경된다")
    void test5() {
        //given

        List<Member> memberList = memberRepository.findAll();
        Member member = memberList.get(0);
        //when
        memberService.modifyPassword(new MemberPasswordModifyRequestDto("test123!", "sangwon123", "sangwon123"), member);
        //then
        assertThat(memberService.passwordConfirm("sangwon123", member)).isTrue();
    }

    @Test
    @DisplayName("임시비밀번호_생성된다")
    void test6() {
        System.out.println(memberService.getTempPassword());
    }

    @Test
    @DisplayName("이메일로_아이디_찾아진다")
    void test7() {
        //given

        //when
        Long id = memberService.findUsername(new UsernameFindRequestDto("dnjsml30@naver.com"));
        //then
        assertThat(memberRepository.findById(id).orElseThrow().getUsername()).isEqualTo("dnjsml30");


    }

}