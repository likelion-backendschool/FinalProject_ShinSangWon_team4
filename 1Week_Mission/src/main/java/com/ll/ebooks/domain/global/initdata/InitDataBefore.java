package com.ll.ebooks.domain.global.initdata;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.dto.request.PostWriteRequestDto;
import com.ll.ebooks.domain.post.service.PostService;

public interface InitDataBefore {
    default void tearUp(MemberService memberService, PostService postService) {
        memberService.join(JoinRequestDto.builder()
                .username("dnjsml30")
                .password("test123!")
                .email("dnjsml30@naver.com")
                .nickname("상원")
                .build());

        memberService.join(JoinRequestDto.builder()
                .username("test123")
                .password("test123")
                .email("test@naver.com")
                .nickname("")
                .build());

        postService.write(PostWriteRequestDto.builder()
                        .subject("안녕하세요")
                        .content("반갑습니다")
                .build(), memberService.findByUsername("dnjsml30").orElseThrow());

        postService.write(PostWriteRequestDto.builder()
                .subject("반갑습니다")
                .content("안녕하세요")
                .build(), memberService.findByUsername("test123").orElseThrow());

    }
}
