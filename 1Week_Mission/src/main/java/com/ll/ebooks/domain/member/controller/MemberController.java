package com.ll.ebooks.domain.member.controller;

import com.ll.ebooks.domain.member.dto.request.JoinRequestDto;
import com.ll.ebooks.domain.member.dto.request.LoginRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberInfoModifyRequestDto;
import com.ll.ebooks.domain.member.dto.request.MemberPasswordModifyRequestDto;
import com.ll.ebooks.domain.member.dto.request.PasswordFindRequestDto;
import com.ll.ebooks.domain.member.dto.request.UsernameFindRequestDto;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

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

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginForm(LoginRequestDto loginRequestDto) {

        return "member/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modifyMemberInformation(MemberInfoModifyRequestDto memberInfoModifyRequestDto, Model model, Principal principal) {

        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());

        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        model.addAttribute("member", optionalMember.get());

        return "member/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modifyMemberInformation(@Valid MemberInfoModifyRequestDto memberInfoModifyRequestDto, Model model, BindingResult bindingResult, Principal principal) {
        //1차 유효성검사
        if(bindingResult.hasErrors()) {
            model.addAttribute("memberInfoModifyRequestDto", memberInfoModifyRequestDto);
            return "member/modify";
        }

        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());
        //2차 유효성검사
        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        Member member = optionalMember.get();

        //비밀번호 검증
        if(!memberService.passwordConfirm(memberInfoModifyRequestDto.getPasswordCheck(), member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        memberService.modify(memberInfoModifyRequestDto, member);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String modifyMemberPassword(MemberPasswordModifyRequestDto memberPasswordModifyRequestDto, Principal principal) {

        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());

        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        return "member/modifyPassword";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String modifyMemberPassword(@Valid MemberPasswordModifyRequestDto memberPasswordModifyRequestDto, BindingResult bindingResult, Principal principal) {
        //1차 유효성검사
        if(bindingResult.hasErrors()) {
            return "member/modifyPassword";
        }

        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());
        //2차 유효성검사
        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        Member member = optionalMember.get();

        //비밀번호 검증
        if(!memberService.passwordConfirm(memberPasswordModifyRequestDto.getOldPassword(), member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        memberService.modifyPassword(memberPasswordModifyRequestDto, member);

        return "redirect:/";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUsername")
    public String findUsername(UsernameFindRequestDto usernameFindRequestDto) {

        return "member/findUsername";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUsername(@Valid UsernameFindRequestDto usernameFindRequestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "member/findUsername";
        }

        memberService.findUsername(usernameFindRequestDto);

        return "redirect:/";

    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String findPassword(PasswordFindRequestDto passwordFindRequestDto) {

        return "member/findPassword";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(@Valid PasswordFindRequestDto passwordFindRequestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "member/findPassword";
        }

        memberService.findPassword(passwordFindRequestDto);


        return "redirect:/";
    }
}
