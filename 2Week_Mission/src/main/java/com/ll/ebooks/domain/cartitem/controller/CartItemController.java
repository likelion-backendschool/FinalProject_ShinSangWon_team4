package com.ll.ebooks.domain.cartitem.controller;

import com.ll.ebooks.domain.cartitem.entity.CartItem;
import com.ll.ebooks.domain.cartitem.service.CartItemService;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.product.entity.Product;
import com.ll.ebooks.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

//TODO 장바구니 삭제 뷰 구현
@RequiredArgsConstructor
@RequestMapping("/cart")
@Controller
public class CartItemController {

    private final CartItemService cartItemService;
    private final ProductService productService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String showCartItemList(Model model, Principal principal) {

        Member member = memberService.findByUsername(principal.getName()).orElseThrow();

        List<CartItem> cartItemList = cartItemService.findAllByMemberId(member.getId());
        model.addAttribute("cartItemList", cartItemList);

        return "cartitem/list";
    }

    @GetMapping("/add/{productId}")
    public String addCartItem(@PathVariable Long productId, Principal principal) {
        Product product = productService.findProductById(productId);
        Member member = memberService.findByUsername(principal.getName()).orElseThrow();

        if(product.getMember().getId() == member.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신의 상품은 등록할 수 없습니다");
        }

        cartItemService.addItem(member, product);

        return "redirect:/cart/list";
    }
}
