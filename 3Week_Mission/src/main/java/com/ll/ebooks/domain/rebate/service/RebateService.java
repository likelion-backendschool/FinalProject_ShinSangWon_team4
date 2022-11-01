package com.ll.ebooks.domain.rebate.service;

import com.ll.ebooks.domain.global.date.service.DateService;
import com.ll.ebooks.domain.order.entity.OrderItem;
import com.ll.ebooks.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RebateService {

    private final DateService dateService;

    private final OrderService orderService;

    public void makeData(String yearMonth) {

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = dateService.getEndDayOfMonth(yearMonth) + " 23:59:59.999999";
        LocalDateTime fromDate = dateService.mapToLocalDateTime(fromDateStr);
        LocalDateTime toDate = dateService.mapToLocalDateTime(toDateStr);

        List<OrderItem> orderItemList = orderService.findAllByPayDateBetween(fromDate, toDate);


    }
}
