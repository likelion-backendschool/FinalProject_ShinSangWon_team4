package com.ll.ebooks.domain.post.service;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.dto.request.PostWriteRequestDto;
import com.ll.ebooks.domain.post.dto.response.PostListResponseDto;
import com.ll.ebooks.domain.post.dto.response.PostResponseDto;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;

    public List<PostListResponseDto> findAll() {
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    public PostResponseDto findById(Long id) {

        Post entity = postRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return new PostResponseDto(entity);
    }

    public Long write(PostWriteRequestDto postWriteRequestDto, Principal principal) {

        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());

        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        Member member = optionalMember.get();

        return postRepository.save(postWriteRequestDto.toEntity(member)).getId();
    }
}
