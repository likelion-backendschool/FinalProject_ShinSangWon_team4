package com.ll.ebooks.domain.product.repository;

import com.ll.ebooks.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByMemberIdOrderByIdDesc(Long memberId);

    List<Product> findAllByOrderByIdDesc();
}
