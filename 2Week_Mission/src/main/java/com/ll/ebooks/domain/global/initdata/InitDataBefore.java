package com.ll.ebooks.domain.global.initdata;

import com.ll.ebooks.domain.cartitem.service.CartItemService;
import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.repository.OrderRepository;
import com.ll.ebooks.domain.order.service.OrderService;
import com.ll.ebooks.domain.post.dto.request.PostWriteRequestDto;
import com.ll.ebooks.domain.post.service.PostService;
import com.ll.ebooks.domain.postkeyword.service.PostKeywordService;
import com.ll.ebooks.domain.product.dto.request.ProductCreateRequestDto;
import com.ll.ebooks.domain.product.entity.Product;
import com.ll.ebooks.domain.product.repository.ProductRepository;
import com.ll.ebooks.domain.product.service.ProductService;

import java.util.Arrays;
import java.util.List;

public interface InitDataBefore {
    default void tearUp(MemberService memberService, PostService postService,
                        ProductService productService, ProductRepository productRepository,
                        PostKeywordService postKeywordService, CartItemService cartItemService,
                        OrderService orderService, OrderRepository orderRepository) {
        class Helper {
            public Order order(Member member, List<Product> products) {
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);

                    cartItemService.addItem(member, product);
                }

                return orderService.createFromCart(member);
            }
        }

        Helper helper = new Helper();

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

        Member member1 = memberService.findByUsername("dnjsml30").orElseThrow();
        Member member2 = memberService.findByUsername("test123").orElseThrow();

        //예치금 충전
        memberService.addCash(member1, 100_000, "무통장입금");
        memberService.addCash(member2, 100_000, "카드결제");

        postService.write(PostWriteRequestDto.builder()
                        .subject("안녕하세요")
                        .content("반갑습니다")
                        .hashTags("#자바 #프로그래밍")
                .build(), member1);

        postService.write(PostWriteRequestDto.builder()
                .subject("반갑습니다")
                .content("안녕하세요")
                .hashTags("#스프링 #스프링부트")
                .build(), member2);

        //상품 생성
        ProductCreateRequestDto productCreateRequestDto = new ProductCreateRequestDto("자바의 정석", 30000);
        productCreateRequestDto.setPostKeyword(postKeywordService.findById(1L).orElseThrow());

        productRepository.save(productCreateRequestDto.toEntity(member1));

        ProductCreateRequestDto productCreateRequestDto2 = new ProductCreateRequestDto("프로그래밍의 정석", 70000);
        productCreateRequestDto2.setPostKeyword(postKeywordService.findById(2L).orElseThrow());

        productRepository.save(productCreateRequestDto2.toEntity(member2));

        Product product1 = productService.findProductById(1L);
        Product product2 = productService.findProductById(2L);

        // 1번 주문
        Order order1 = helper.order(member1, Arrays.asList(
                        product1,
                        product2
                )
        );





    }}
