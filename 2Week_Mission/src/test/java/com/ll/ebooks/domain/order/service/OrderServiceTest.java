package com.ll.ebooks.domain.order.service;

import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.entity.OrderStatus;
import com.ll.ebooks.domain.order.repository.OrderRepository;
import com.ll.ebooks.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("1번_주문_존재한다")
    void test1() {
        Order order = orderRepository.findById(1L).orElse(null);

        assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("1번_주문_결제된다")
    void test2() {
        Order order = orderRepository.findById(1L).orElse(null);

        int orderPayPrice = order.getTotalPayPrice();
        System.out.println("결제전 : %s".formatted(order.getOrderStatus()));
        orderService.payOnlyRestCash(order);
        System.out.println("결제후 : %s".formatted(order.getOrderStatus()));
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAID);
    }

}