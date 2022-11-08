package com.ll.ebooks.domain.mybook.repository;

import com.ll.ebooks.domain.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {

    List<MyBook> findAllByMemberIdOrderByIdDesc(Long memberId);

    Optional<MyBook> findByMemberIdAndProductId(Long memberId, Long productId);
}
