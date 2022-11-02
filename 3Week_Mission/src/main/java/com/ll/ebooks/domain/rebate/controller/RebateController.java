package com.ll.ebooks.domain.rebate.controller;

import com.ll.ebooks.domain.rebate.entity.RebateOrderItem;
import com.ll.ebooks.domain.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public String makeRebateData(String yearMonth) {

        rebateService.makeData(yearMonth);
        return "redirect:/adm/rebate/rebateOrderItemList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/rebate/rebateOrderItemList")
    public String showRebateOrderItemList(String yearMonth, Model model) {

        if(yearMonth == null) {
            yearMonth = "2022-11";
        }

        List<RebateOrderItem> rebateOrderItemList = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);
        model.addAttribute("rebateOrderItemList", rebateOrderItemList);
        return "rebate/rebateOrderItemList";
    }
}
