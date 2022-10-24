package com.ll.ebooks.domain.post.service;

import com.ll.ebooks.domain.member.repository.MemberRepository;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.post.repository.PostRepository;
import com.ll.ebooks.domain.posttag.entity.PostTag;
import com.ll.ebooks.domain.posttag.repository.PostTagRepository;
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
class PostServiceTest {

    private final PostService postService;
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PostTagRepository postTagRepository;

    @Autowired
    public PostServiceTest(PostService postService, PostRepository postRepository, MemberService memberService, MemberRepository memberRepository, PostTagRepository postTagRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.postTagRepository = postTagRepository;
    }

    @BeforeEach
    void tearUp() {
    }

    @Test
    @DisplayName("게시물_작성된다")
    void test1() {
        //given

        // InitDataBefore

        //when

        List<Post> postList = postRepository.findAll();
        Post post = postList.get(0);

        //then
        assertThat(post.getSubject()).isEqualTo("안녕하세요");

    }

    @Test
    @DisplayName("1번_게시물에는_태그가_2개_존재한다")
    void test2() {

        List<Post> postList = postRepository.findAll();
        Post post = postList.get(0);
        //
        List<PostTag> hashTags = postTagRepository.findAllByPostId(post.getId());
        //
        assertThat(hashTags.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("1번_게시물의_해시태그_수정된다")
    void test3() {
        
    }


}