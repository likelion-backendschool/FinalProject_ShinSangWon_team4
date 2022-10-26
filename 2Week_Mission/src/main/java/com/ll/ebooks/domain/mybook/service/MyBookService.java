package com.ll.ebooks.domain.mybook.service;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.mybook.dto.response.MyBookListResponseDto;
import com.ll.ebooks.domain.mybook.entity.MyBook;
import com.ll.ebooks.domain.mybook.repository.MyBookRepository;
import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.entity.OrderItem;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MyBookService {

    private final MyBookRepository myBookRepository;

    public List<MyBookListResponseDto> findAllByMemberId(Long memberId) {
        return myBookRepository.findAllByMemberIdOrderByIdDesc(memberId).stream()
                .map(MyBookListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long RegisteringBook(Member member, Order order) {

        List<OrderItem> orderItemList = order.getOrderItems();

        for(OrderItem orderItem : orderItemList) {
            Product product = orderItem.getProduct();
            MyBook myBook = MyBook.builder()
                    .member(member)
                    .product(product)
                    .build();
            myBookRepository.save(myBook);
        }

        return order.getId();
    }
}
