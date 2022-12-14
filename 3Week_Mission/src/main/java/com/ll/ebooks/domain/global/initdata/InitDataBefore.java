package com.ll.ebooks.domain.global.initdata;

import com.ll.ebooks.domain.cartitem.service.CartItemService;
import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.entity.Role;
import com.ll.ebooks.domain.member.repository.MemberRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

public interface InitDataBefore {
    default void tearUp(MemberService memberService, MemberRepository memberRepository,
                        PostService postService, ProductService productService,
                        ProductRepository productRepository, PostKeywordService postKeywordService,
                        CartItemService cartItemService, OrderService orderService,
                        OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
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
                .nickname("??????")
                .build());

        memberService.join(JoinRequestDto.builder()
                .username("test123")
                .password("test123")
                .email("test@naver.com")
                .nickname("")
                .build());
        Member member1 = memberService.findByUsername("dnjsml30").orElseThrow();
        Member member2 = memberService.findByUsername("test123").orElseThrow();
        //????????? ?????? ??????
        Member member3 = Member.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .email("admin@mutbooks.com")
                .nickname("?????????")
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member3);


        //????????? ??????
        memberService.addCash(member1, 100_000, "???????????????");
        memberService.addCash(member2, 100_000, "????????????");

        postService.write(PostWriteRequestDto.builder()
                        .subject("???????????????")
                        .content("???????????????")
                        .hashTags("#?????? #???????????????")
                .build(), member1);

        postService.write(PostWriteRequestDto.builder()
                .subject("???????????????")
                .content("???????????????")
                .hashTags("#????????? #???????????????")
                .build(), member2);

        //?????? ??????
        ProductCreateRequestDto productCreateRequestDto = new ProductCreateRequestDto("????????? ??????", 30000);
        productCreateRequestDto.setPostKeyword(postKeywordService.findById(1L).orElseThrow());

        productRepository.save(productCreateRequestDto.toEntity(member1));

        ProductCreateRequestDto productCreateRequestDto2 = new ProductCreateRequestDto("?????????????????? ??????", 70000);
        productCreateRequestDto2.setPostKeyword(postKeywordService.findById(2L).orElseThrow());

        productRepository.save(productCreateRequestDto2.toEntity(member2));

        ProductCreateRequestDto productCreateRequestDto3 = new ProductCreateRequestDto("???????????? ??????", 700000);
        productCreateRequestDto3.setPostKeyword(postKeywordService.findById(2L).orElseThrow());

        productRepository.save(productCreateRequestDto3.toEntity(member2));

        ProductCreateRequestDto productCreateRequestDto4 = new ProductCreateRequestDto("????????????????????? ??????", 30000);
        productCreateRequestDto4.setPostKeyword(postKeywordService.findById(2L).orElseThrow());

        productRepository.save(productCreateRequestDto4.toEntity(member2));


        Product product1 = productService.findProductById(1L);
        Product product2 = productService.findProductById(2L);

        // 1??? ??????
        Order order1 = helper.order(member1, Arrays.asList(
                        product1,
                        product2
                )
        );





    }}
