package com.ll.ebooks.domain.posttag.repository;

import com.ll.ebooks.domain.posttag.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    Optional<PostTag> findByPostIdAndPostKeywordId(Long id, Long id1);

    List<PostTag> findAllByPostId(Long PostId);
}
