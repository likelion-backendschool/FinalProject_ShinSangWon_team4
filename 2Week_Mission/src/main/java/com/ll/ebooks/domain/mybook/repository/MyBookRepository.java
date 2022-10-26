package com.ll.ebooks.domain.mybook.repository;

import com.ll.ebooks.domain.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {

    List<MyBook> findAllByMemberIdOrderByIdDesc(Long memberId);
}
