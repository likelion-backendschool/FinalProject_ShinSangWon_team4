package com.ll.ebooks.domain.order.repository;

import com.ll.ebooks.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByMemberIdOrderByIdDesc(Long memberId);
}
