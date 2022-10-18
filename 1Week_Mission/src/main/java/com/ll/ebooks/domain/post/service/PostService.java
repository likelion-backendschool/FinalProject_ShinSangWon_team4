package com.ll.ebooks.domain.post.service;

import com.ll.ebooks.domain.post.dto.response.PostListResponseDto;
import com.ll.ebooks.domain.post.dto.response.PostResponseDto;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;

    public List<PostListResponseDto> findAll() {
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    public PostResponseDto findById(Long id) {

        Post entity = postRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return new PostResponseDto(entity);
    }
}
