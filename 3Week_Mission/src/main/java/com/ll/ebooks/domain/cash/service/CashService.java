package com.ll.ebooks.domain.cash.service;

import com.ll.ebooks.domain.cash.entity.CashLog;
import com.ll.ebooks.domain.cash.repository.CashLogRepository;
import com.ll.ebooks.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CashService {

    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member, int price, String eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        return cashLog;
    }

    public CashLog findById(long cashLogId) {
        return cashLogRepository.findById(cashLogId).orElseThrow(NoSuchElementException::new);
    }
}
