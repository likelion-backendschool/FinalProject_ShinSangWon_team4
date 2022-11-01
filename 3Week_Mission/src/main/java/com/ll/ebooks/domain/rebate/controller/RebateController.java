package com.ll.ebooks.domain.rebate.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/adm")
@Controller
public class RebateController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/rebate/makeData")
    public String showAdminMainPage() {

        return "rebate/makeData";
    }
}
