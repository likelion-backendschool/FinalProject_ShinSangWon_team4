package com.ll.ebooks.domain.rebate.controller;

import com.ll.ebooks.domain.rebate.entity.RebateOrderItem;
import com.ll.ebooks.domain.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
@Controller
public class RebateController {

    private final RebateService rebateService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/makeData")
    public String showRebateMakeData() {

        return "rebate/makeData";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/makeData")
    public String makeRebateData(String yearMonth) {

        rebateService.makeData(yearMonth);
        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(yearMonth);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/rebateOrderItemList")
    public String showRebateOrderItemList(String yearMonth, Model model) {

        List<RebateOrderItem> rebateOrderItemList = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);
        model.addAttribute("rebateOrderItemList", rebateOrderItemList);
        return "rebate/rebateOrderItemList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/rebateOne/{orderItemId}")
    public String rebateOne(@PathVariable Long orderItemId, HttpServletRequest req) {
        String referer = req.getHeader("Referer");
        String url[] = referer.split("yearMonth=");
        String yearMonth = url[1];
        rebateService.rebate(orderItemId);

        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(yearMonth);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/rebate")
    public String rebate(String ids, HttpServletRequest req) {

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(rebateService::rebate);

        String referer = req.getHeader("Referer");
        String url[] = referer.split("yearMonth=");
        String yearMonth = url[1];
        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(yearMonth);
    }
}
