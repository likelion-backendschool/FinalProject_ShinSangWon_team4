package com.ll.ebooks.domain.order.service;

import com.ll.ebooks.domain.cartitem.entity.CartItem;
import com.ll.ebooks.domain.cartitem.service.CartItemService;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.order.dto.response.OrderListResponseDto;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.entity.OrderItem;
import com.ll.ebooks.domain.order.exception.NotEnoughMoneyException;
import com.ll.ebooks.domain.order.exception.RefundTimeOutException;
import com.ll.ebooks.domain.order.repository.OrderItemRepository;
import com.ll.ebooks.domain.order.repository.OrderRepository;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final MemberService memberService;
    private final OrderItemRepository orderItemRepository;
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
    public Order createSingleOrder(Long id, Member loginMember) {
        CartItem cartItem = cartItemService.findById(id);
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = cartItem.getProduct();

        if(product.isOrderable()) {
            orderItems.add(new OrderItem(product));
            cartItemService.removeItem(cartItem);
        }

        return create(loginMember, orderItems);
    }

    @Transactional
    public Order create(Member member, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .member(member)
                .build();

        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.makeName();
        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void payOnlyRestCash(Order order) {
        Member member = order.getMember(); // 구매자

        int restCash = member.getRestCash();
        int payPrice = order.getTotalPayPrice();

        if(payPrice > restCash) {
            throw new NotEnoughMoneyException();
        }

        memberService.addCash(member, payPrice * -1, "주문결제_예치금");

        order.setPaymentDone();
        orderRepository.save(order);

    }

    @Transactional
    public void payTossPayments(Order order, long restPayCash) {

        Member member = order.getMember(); // 구매자
        int payPrice = order.getTotalPayPrice();

        //Tosspayments를 예치금 시스템에 편입시키기
        int pgPayPrice = (int) (payPrice - restPayCash);
        memberService.addCash(member, pgPayPrice, "주문결제충전_토스페이먼츠");
        memberService.addCash(member, pgPayPrice * -1, "주문결제_토스페이먼츠");

        if( restPayCash > 0) {
            memberService.addCash(member, (int) restPayCash * -1, "주문결제_토스페이먼츠");
        }

        order.setPaymentDone();
        orderRepository.save(order);

    }

    @Transactional
    public void refund(Order order) {

        boolean isRefundable = getRefundPossibility(order);

        if(!isRefundable) {
            throw new RefundTimeOutException();
        }

        int payPrice = order.getTotalPayPrice();
        memberService.addCash(order.getMember(), payPrice, "주문환불_예치금환불");

        order.setRefundDone();
        orderRepository.save(order);

    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    @Transactional
    public void cancel(Order order) {
        order.setCancel();
    }

    //환불 가능 여부
    public boolean getRefundPossibility(Order order) {

        LocalDateTime orderTime = order.getPayDate();
        LocalDateTime targetTime = orderTime.plusMinutes(10);

        //지금 시각이 주문 시간+10분 전이므로, 환불 가능
        if(LocalDateTime.now().isBefore(targetTime)) {
            return true;
        }

        return false;

    }

    public List<OrderListResponseDto> findAllByMemberId(Long memberId) {
        return orderRepository.findAllByMemberIdOrderByIdDesc(memberId).stream()
                .map(OrderListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<OrderItem> findAllByPayDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }
}
