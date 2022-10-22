package com.ll.ebooks.domain.posttag.service;

import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import com.ll.ebooks.domain.postkeyword.service.PostKeywordService;
import com.ll.ebooks.domain.posttag.entity.PostTag;
import com.ll.ebooks.domain.posttag.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostTagService {

    private final PostKeywordService postKeywordService;
    private final PostTagRepository postTagRepository;
    public void mapToPostHashTags(Post savedPost, String hashTags) {

        List<String> hashTagList = Arrays.stream(hashTags.split("#"))
                .map(String::trim)
                .filter(x -> x.length() > 0)
                .collect(Collectors.toList());

        hashTagList.forEach(hashTag -> {
            saveHashTag(savedPost, hashTag);
        });
    }

    private PostTag saveHashTag(Post post, String content) {
        PostKeyword postKeyword = postKeywordService.save(content);

        Optional<PostTag> opPostTag = postTagRepository.findByPostIdAndPostKeywordId(post.getId(), postKeyword.getId());

        if(opPostTag.isPresent()) {
            return opPostTag.get();
        }

        PostTag postTag = PostTag.builder()
                .post(post)
                .member(post.getMember())
                .postKeyword(postKeyword)
                .build();

        postTagRepository.save(postTag);

        return postTag;
    }
}
