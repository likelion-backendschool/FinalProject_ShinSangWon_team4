package com.ll.ebooks.domain.member.controller;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String JoinForm(JoinRequestDto joinRequestDto) {


        return "member/JoinForm";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String Join(@Valid JoinRequestDto joinRequestDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("joinRequestDto", joinRequestDto);
            return "member/JoinForm";
        }

        if(!joinRequestDto.getPassword().equals(joinRequestDto.getPasswordCheck())) {
            bindingResult.rejectValue("password", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return "member/JoinForm";
        }

        memberService.join(joinRequestDto);

        return "redirect:/";
    }
}
