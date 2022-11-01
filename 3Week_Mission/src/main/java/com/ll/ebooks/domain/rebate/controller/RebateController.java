package com.ll.ebooks.domain.rebate.controller;

import com.ll.ebooks.domain.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@RequestMapping("/adm")
@Controller
public class RebateController {

    private final RebateService rebateService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/rebate/makeData")
    public String showRebateMakeData() {

        return "rebate/makeData";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/rebate/makeData")
    @ResponseBody
    public String makeRebateData(String yearMonth) {

        rebateService.makeData(yearMonth);

        return "rebate/makeData";
    }
}
