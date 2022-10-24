package com.ll.ebooks.domain.product.controller;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import com.ll.ebooks.domain.postkeyword.service.PostKeywordService;
import com.ll.ebooks.domain.product.dto.request.ProductCreateRequestDto;
import com.ll.ebooks.domain.product.dto.request.ProductModifyRequestDto;
import com.ll.ebooks.domain.product.dto.response.ProductResponseDto;
import com.ll.ebooks.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/product")
@Controller
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;
    private final PostKeywordService postKeywordService;

    @PreAuthorize("isAuthenticated() and hasAuthority('WRITER')")
    @GetMapping("/create")
    public String showCreate(ProductCreateRequestDto productCreateRequestDto, Model model, Principal principal) {

        List<PostKeyword> postKeywordList = postKeywordService.findByMemberId(memberService.findByUsername(principal.getName()).get().getId());
        model.addAttribute("postKeywords", postKeywordList);
        return "product/create";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('WRITER')")
    @PostMapping("/create")
    public String create(@Valid ProductCreateRequestDto productCreateRequestDto, Principal principal) {

        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());

        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        productService.write(productCreateRequestDto, optionalMember.get());

        return "redirect:/product/list";
    }
    
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {

        ProductResponseDto product = productService.findById(id);
        List<Post> posts = productService.findPostsByProduct(product);

        model.addAttribute("posts", posts);
        model.addAttribute("product", product);
        
        return "product/detail";
        
    }

    @GetMapping("/list")
    public String showList(Model model, Principal principal) {

        Optional<Member> optionalMember = memberService.findByUsername(principal.getName());

        if(optionalMember.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 후 이용해주세요");
        }

        model.addAttribute("productList", productService.findAllByMemberId(optionalMember.get().getId()));
        return "product/list";

    }

    @GetMapping("/{id}/modify")
    public String showModify(Model model, @PathVariable Long id, ProductModifyRequestDto productModifyRequestDto, Principal principal) {
        if(!productService.isAuthorized(id, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        model.addAttribute("product", productService.findById(id));
        return "product/modify";
    }

    @PostMapping("/{id}/modify")
    public String modify(@PathVariable Long id, @Valid ProductModifyRequestDto productModifyRequestDto, BindingResult bindingResult, Principal principal) {

        if(bindingResult.hasErrors()) {
            return "product/modify";
        }

        if(!productService.isAuthorized(id, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        productService.modify(productModifyRequestDto, id);

        return "redirect:/product/%d".formatted(id);
    }

    @GetMapping("/{id}/remove")
    public String remove(@PathVariable Long id, Principal principal) {

        if(!productService.isAuthorized(id, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        productService.remove(id);

        return "redirect:/product/list";
    }



}
