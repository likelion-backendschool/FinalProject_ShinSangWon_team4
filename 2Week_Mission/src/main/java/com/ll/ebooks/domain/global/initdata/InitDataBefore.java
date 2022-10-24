package com.ll.ebooks.domain.global.initdata;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.dto.request.PostWriteRequestDto;
import com.ll.ebooks.domain.post.service.PostService;
import com.ll.ebooks.domain.postkeyword.service.PostKeywordService;
import com.ll.ebooks.domain.product.dto.request.ProductCreateRequestDto;
import com.ll.ebooks.domain.product.repository.ProductRepository;
import com.ll.ebooks.domain.product.service.ProductService;

public interface InitDataBefore {
    default void tearUp(MemberService memberService, PostService postService,
                        ProductService productService, ProductRepository productRepository,
                        PostKeywordService postKeywordService) {
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
                        .hashTags("#자바 #프로그래밍")
                .build(), memberService.findByUsername("dnjsml30").orElseThrow());

        postService.write(PostWriteRequestDto.builder()
                .subject("반갑습니다")
                .content("안녕하세요")
                .hashTags("#스프링 #스프링부트")
                .build(), memberService.findByUsername("test123").orElseThrow());

        ProductCreateRequestDto productCreateRequestDto = new ProductCreateRequestDto("자바의 정석", 30000);
        productCreateRequestDto.setPostKeyword(postKeywordService.findById(1L).orElseThrow());

        productRepository.save(productCreateRequestDto.toEntity(memberService.findByUsername("dnjsml30").orElseThrow()));

    }
}
