package com.ll.ebooks.domain.rebate.service;

import com.ll.ebooks.domain.global.date.service.DateService;
import com.ll.ebooks.domain.order.entity.OrderItem;
import com.ll.ebooks.domain.order.service.OrderService;
import com.ll.ebooks.domain.rebate.entity.RebateOrderItem;
import com.ll.ebooks.domain.rebate.repository.RebateOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RebateService {

    private final DateService dateService;
    private final OrderService orderService;
    private final RebateOrderItemRepository rebateOrderItemRepository;

    @Transactional
    public void makeData(String yearMonth) {

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = dateService.getEndDayOfMonth(yearMonth) + " 23:59:59.999999";
        LocalDateTime fromDate = dateService.mapToLocalDateTime(fromDateStr);
        LocalDateTime toDate = dateService.mapToLocalDateTime(toDateStr);

        List<OrderItem> orderItemList = orderService.findAllByPayDateBetween(fromDate, toDate);

        List<RebateOrderItem> rebateOrderItemList = orderItemList
                .stream()
                .map(this::mapToRebateOrderItem)
                .collect(Collectors.toList());

        rebateOrderItemList.forEach(this::makeRebateOrderItem);
    }

    public RebateOrderItem mapToRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }

    @Transactional
    public void makeRebateOrderItem(RebateOrderItem rebateOrderItem) {
        RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(rebateOrderItem.getOrderItem().getId())
                .orElse(null);

        if(oldRebateOrderItem != null) {
            rebateOrderItemRepository.delete(oldRebateOrderItem);
        }

        rebateOrderItemRepository.save(rebateOrderItem);
    }

    public List<RebateOrderItem> findRebateOrderItemsByPayDateIn(String yearMonth) {
        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = dateService.getEndDayOfMonth(yearMonth) + " 23:59:59.999999";
        LocalDateTime fromDate = dateService.mapToLocalDateTime(fromDateStr);
        LocalDateTime toDate = dateService.mapToLocalDateTime(toDateStr);

        return rebateOrderItemRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }
}
