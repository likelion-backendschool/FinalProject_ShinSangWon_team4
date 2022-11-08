package com.ll.ebooks.domain.mybook.controller;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.mybook.service.MyBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@RequestMapping("/myBook")
@Controller
public class MyBookController {

    private final MyBookService myBookService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String showMyBookList(Model model, Principal principal) {

        Member loginMember = memberService.findByUsername(principal.getName())
                .orElseThrow(() -> new NoSuchElementException("비정상적인 접근입니다."));

        model.addAttribute("myBookList", myBookService.findAllByMemberId(loginMember.getId()));

        return "mybook/list";
    }
}
