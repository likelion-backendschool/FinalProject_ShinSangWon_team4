package com.ll.ebooks.domain.product.service;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import com.ll.ebooks.domain.postkeyword.service.PostKeywordService;
import com.ll.ebooks.domain.posttag.entity.PostTag;
import com.ll.ebooks.domain.posttag.service.PostTagService;
import com.ll.ebooks.domain.product.dto.request.ProductCreateRequestDto;
import com.ll.ebooks.domain.product.dto.request.ProductModifyRequestDto;
import com.ll.ebooks.domain.product.dto.response.ProductListResponseDto;
import com.ll.ebooks.domain.product.dto.response.ProductResponseDto;
import com.ll.ebooks.domain.product.entity.Product;
import com.ll.ebooks.domain.product.repository.ProductRepository;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final PostKeywordService postKeywordService;
    private final PostTagService postTagService;
    private final MemberService memberService;

    @Transactional
    public Long write(ProductCreateRequestDto productCreateRequestDto, Member member) {

        PostKeyword postKeyword = postKeywordService.findById(productCreateRequestDto.getPostKeywordId()).orElseThrow();
        productCreateRequestDto.setPostKeyword(postKeyword);

        return productRepository.save(productCreateRequestDto.toEntity(member)).getId();
    }

    public ProductResponseDto findById(long id) {

        Product entity = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("???????????? ???????????? ????????????."));

        return new ProductResponseDto(entity);
    }

    public List<Post> findPostsByProduct(ProductResponseDto product) {
        Member member = product.getMember();
        PostKeyword postKeyword = product.getPostKeyword();
        List<PostTag> postTags = postTagService.getPostTags(member.getId(), postKeyword.getId());

        return postTags
                .stream()
                .map(PostTag::getPost)
                .collect(Collectors.toList());
    }

    public List<ProductListResponseDto> findAllByMemberId(Long memberId) {
        return productRepository.findAllByMemberIdOrderByIdDesc(memberId).stream()
                .map(ProductListResponseDto::new)
                .collect(Collectors.toList());
    }

    public boolean isAuthorized(Long id, Principal principal) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("???????????? ???????????? ????????????."));
        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());

        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????? ??? ??????????????????");
        }

        Member member = optionalMember.get();

        if(product.getMember().equals(member)) {
            return true;
        }

        return false;
    }

    @Transactional
    public Long modify(ProductModifyRequestDto productModifyRequestDto, Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("???????????? ???????????? ????????????."));

        product.modify(productModifyRequestDto.getSubject(), productModifyRequestDto.getPrice());

        return id;

    }

    @Transactional
    public void remove(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("???????????? ???????????? ????????????."));

        productRepository.delete(product);
    }

}
