package com.ll.ebooks.domain.rebate.service;

import com.ll.ebooks.domain.cash.entity.CashLog;
import com.ll.ebooks.domain.cash.service.CashService;
import com.ll.ebooks.domain.global.date.service.DateService;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.order.entity.OrderItem;
import com.ll.ebooks.domain.order.service.OrderService;
import com.ll.ebooks.domain.rebate.entity.RebateOrderItem;
import com.ll.ebooks.domain.rebate.repository.RebateOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RebateService {

    private final DateService dateService;
    private final OrderService orderService;
    private final RebateOrderItemRepository rebateOrderItemRepository;
    private final MemberService memberService;
    private final CashService cashService;

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

    @Transactional
    public Long rebate(Long orderItemId) {
        RebateOrderItem rebateOrderItem = rebateOrderItemRepository.findByOrderItemId(orderItemId)
                .orElseThrow(NoSuchElementException::new);

        if(!rebateOrderItem.isRebateAvailable()) {
            throw new RuntimeException("정산을 할 수 없습니다");
        }

        int calculateRebatePrice = rebateOrderItem.calculateRebatePrice();

        Long cashLogId = memberService.addCash(rebateOrderItem.getProduct().getMember(), calculateRebatePrice, "정산_%d_지급_예치금".formatted(rebateOrderItem.getId()));

        CashLog cashLog = cashService.findById(cashLogId);

        rebateOrderItem.setRebateDone(cashLog);

        return rebateOrderItem.getId();
    }


}
