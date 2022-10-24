package com.ll.ebooks.domain.cartitem.service;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.repository.MemberRepository;
import com.ll.ebooks.domain.post.service.PostService;
import com.ll.ebooks.domain.product.entity.Product;
import com.ll.ebooks.domain.product.repository.ProductRepository;
import com.ll.ebooks.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CartItemServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemService cartItemService;


    @Test
    @DisplayName("상품_장바구니에_담긴다")
    void test1() {
        Member buyer = memberRepository.findByUsername("test123").get();

        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        CartItem cartItem1 = cartItemService.addItem(buyer, product1);
        CartItem cartItem2 = cartItemService.addItem(buyer, product2);

    }
}