package com.ll.ebooks.domain.cartitem.service;

import com.ll.ebooks.domain.cartitem.entity.CartItem;
import com.ll.ebooks.domain.cartitem.repository.CartItemRepository;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addItem(Member member, Product product) {
        CartItem cartItem = cartItemRepository.findByMemberIdAndProductId(member.getId(), product.getId()).orElse(null);

        if(cartItem != null) {
            return cartItem;
        }

        CartItem newCartItem = CartItem.builder()
                .member(member)
                .product(product)
                .build();

        cartItemRepository.save(newCartItem);

        return newCartItem;
    }

}
