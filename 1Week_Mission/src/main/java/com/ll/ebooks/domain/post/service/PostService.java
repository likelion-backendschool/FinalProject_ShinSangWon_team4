package com.ll.ebooks.domain.post.service;

import com.ll.ebooks.domain.global.markdown.MarkdownService;
import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.dto.request.PostModifyRequestDto;
import com.ll.ebooks.domain.post.dto.request.PostWriteRequestDto;
import com.ll.ebooks.domain.post.dto.response.PostListResponseDto;
import com.ll.ebooks.domain.post.dto.response.PostResponseDto;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.post.repository.PostRepository;
import com.ll.ebooks.domain.posttag.service.PostTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MarkdownService markdownService;
    private final PostTagService postTagService;

    public List<PostListResponseDto> findAll() {
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    public PostResponseDto findById(Long id) {

        Post entity = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));

        return new PostResponseDto(entity);
    }
    @Transactional
    public Long write(PostWriteRequestDto postWriteRequestDto, Member member) {

        postWriteRequestDto.setContentHtml(markdownService.toMarkdown(postWriteRequestDto.getContent()));
        Post savedPost = postRepository.save(postWriteRequestDto.toEntity(member));
        postTagService.mapToPostHashTags(savedPost, postWriteRequestDto.getHashTags());

        return savedPost.getId();
    }
    @Transactional
    public Long modify(PostModifyRequestDto postModifyRequestDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));

        post.modify(postModifyRequestDto.getSubject(), postModifyRequestDto.getContent(), markdownService.toMarkdown(postModifyRequestDto.getContent()));

        return id;

    }
    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));

        postRepository.delete(post);
    }
    //글 수정, 삭제 권한이 있는지 검사하는  로직
    public boolean isAuthorized(Long id, Principal principal) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));
        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());

        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        Member member = optionalMember.get();

        if(post.getMember().equals(member)) {
            return true;
        }

        return false;
    }

    public List<PostListResponseDto> findMainPageList() {
        return postRepository.findTop100ByOrderByIdDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }
}
