package com.ll.ebooks.domain.post.controller;

import com.ll.ebooks.domain.post.dto.request.PostModifyRequestDto;
import com.ll.ebooks.domain.post.dto.request.PostWriteRequestDto;
import com.ll.ebooks.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/post")
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    public String showList(Model model) {

        model.addAttribute("postList", postService.findAll());
        return "post/list";
    }

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable Long id) {

        model.addAttribute("post", postService.findById(id));

        return "post/detail";
    }

    @GetMapping("/write")
    public String writeForm(PostWriteRequestDto postWriteRequestDto) {
        return "post/write";
    }

    @PostMapping("/write")
    public String write(@Valid PostWriteRequestDto postWriteRequestDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "post/write";
        }

        postService.write(postWriteRequestDto, principal);

        return "redirect:post/list";

    }

    @GetMapping("/{id}/modify")
    public String modifyForm(Model model, @PathVariable Long id, PostModifyRequestDto postModifyRequestDto, Principal principal) {

        if(!postService.isAuthorized(id, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        model.addAttribute("post", postService.findById(id));
        return "post/modify";
    }

    @PostMapping("/{id}/modify")
    public String modify(@PathVariable Long id, @Valid PostModifyRequestDto postModifyRequestDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "post/modify";
        }

        if(!postService.isAuthorized(id, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        postService.modify(postModifyRequestDto, id);

        return "redirect:post/%d/modify.formatted(id)";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {

        if(!postService.isAuthorized(id, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        postService.delete(id);

        return "redirect:/post/list";
    }
}
