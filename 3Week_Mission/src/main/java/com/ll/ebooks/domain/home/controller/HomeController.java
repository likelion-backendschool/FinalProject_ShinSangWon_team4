package com.ll.ebooks.domain.home.controller;

import com.ll.ebooks.domain.post.service.PostService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final PostService postService;
    @GetMapping("/")
    public String showMainPage(Model model) {

        model.addAttribute("postList", postService.findMainPageList());

        return "home/main";
    }
}
