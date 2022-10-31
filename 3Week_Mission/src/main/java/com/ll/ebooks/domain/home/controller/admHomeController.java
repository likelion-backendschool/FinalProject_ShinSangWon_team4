package com.ll.ebooks.domain.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/adm")
@Controller
public class admHomeController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/home/main")
    public String showAdminMainPage() {

        return "adm/main";
    }
}
