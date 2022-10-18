package com.ll.ebooks.domain.post.service;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.repository.MemberRepository;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class PostServiceTest {

    private final PostService postService;
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public PostServiceTest(PostService postService, PostRepository postRepository, MemberService memberService, MemberRepository memberRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @BeforeEach
    void tearUp() {
        memberService.join(JoinRequestDto.builder()
                .username("dnjsml30")
                .password("test123!")
                .email("dnjsml30@naver.com")
                .nickname("상원")
                .build());
    }

    @Test
    @DisplayName("게시물_작성된다")
    void test1() {

    }


}