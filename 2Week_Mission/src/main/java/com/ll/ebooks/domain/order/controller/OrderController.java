package com.ll.ebooks.domain.order.controller;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/{id}")
    public String showOrderDetail(@PathVariable Long id, Principal principal, Model model) {

        Order order = orderService.findById(id);

        if(order == null) {
            throw new NoSuchElementException("해당 주문이 존재하지 않습니다.");
        }

        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        if(!order.getMember().equals(loginMember)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인의 주문만 볼 수 있습니다.");
        }

        model.addAttribute("order", order);
        return "order/detail";
    }

}
