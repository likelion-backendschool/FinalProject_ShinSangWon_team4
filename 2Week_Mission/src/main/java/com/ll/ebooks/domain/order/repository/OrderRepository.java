package com.ll.ebooks.domain.order.repository;

import com.ll.ebooks.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
