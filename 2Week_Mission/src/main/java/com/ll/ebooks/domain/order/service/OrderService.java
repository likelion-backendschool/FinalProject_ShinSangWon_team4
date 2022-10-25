package com.ll.ebooks.domain.order.service;

import com.ll.ebooks.domain.cartitem.entity.CartItem;
import com.ll.ebooks.domain.cartitem.service.CartItemService;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.entity.OrderItem;
import com.ll.ebooks.domain.order.repository.OrderRepository;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final MemberService memberService;

    @Transactional
    public Order createFromCart(Member member) {

        List<CartItem> cartItems = cartItemService.findItemsByMember(member);
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if(product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }

            cartItemService.removeItem(cartItem);

        }

        return create(member, orderItems);
    }

    @Transactional
    public Order create(Member member, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .member(member)
                .build();

        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void payOnlyRestCash(Order order) {
        Member member = order.getMember(); // 구매자

        int restCash = member.getRestCash();
        int payPrice = order.getTotalPayPrice();

        if(payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(member, payPrice * -1, "주문결제_예치금결제");

        order.setPaymentDone();
        orderRepository.save(order);

    }

    @Transactional
    public void refund(Order order) {
        int payPrice = order.getTotalPayPrice();
        memberService.addCash(order.getMember(), payPrice, "주문환불_예치금환불");

        order.setRefundDone();
        orderRepository.save(order);

    }
}
