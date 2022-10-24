package com.ll.ebooks.domain.cartitem.repository;

import com.ll.ebooks.domain.cartitem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByMemberIdAndProductId(Long id, Long id1);

    boolean existsByMemberIdAndProductId(Long memberId, Long productId1);

    List<CartItem> findAllByMemberIdOrderByIdDesc(Long memberId);
}
