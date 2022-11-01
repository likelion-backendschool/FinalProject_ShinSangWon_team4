package com.ll.ebooks.domain.rebate.repository;

import com.ll.ebooks.domain.rebate.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RebateOrderItemRepository extends JpaRepository<RebateOrderItem, Long> {

    Optional<RebateOrderItem> findByOrderItemId(Long orderItemId);
}
