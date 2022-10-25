package com.ll.ebooks.domain.cartitem.service;

import com.ll.ebooks.domain.cartitem.entity.CartItem;
import com.ll.ebooks.domain.cartitem.repository.CartItemRepository;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    public boolean removeItem(Member member, Product product) {

        CartItem cartItem = cartItemRepository.findByMemberIdAndProductId(member.getId(), product.getId()).orElseThrow(NoSuchElementException::new);

        if( cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }

        return false;

    }

    @Transactional
    public void removeItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    public boolean hasItem(Member member, Product product) {

        return cartItemRepository.existsByMemberIdAndProductId(member.getId(), product.getId());
    }

    public List<CartItem> findAllByMemberId(Long id) {
        return cartItemRepository.findAllByMemberIdOrderByIdDesc(id);
    }

    public List<CartItem> findItemsByMember(Member member) {
        return cartItemRepository.findAllByMemberId(member.getId());
    }

    public CartItem findById(Long cartitemId) {
        return cartItemRepository.findById(cartitemId).orElse(null);
    }

}
