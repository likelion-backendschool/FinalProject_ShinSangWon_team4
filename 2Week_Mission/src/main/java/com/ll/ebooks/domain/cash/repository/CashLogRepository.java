package com.ll.ebooks.domain.cash.repository;

import com.ll.ebooks.domain.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
