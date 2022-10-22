package com.ll.ebooks.domain.posttag.service;

import com.ll.ebooks.domain.member.entity.Member;
import com.ll.ebooks.domain.post.entity.Post;
import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import com.ll.ebooks.domain.postkeyword.service.PostKeywordService;
import com.ll.ebooks.domain.posttag.entity.PostTag;
import com.ll.ebooks.domain.posttag.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<PostTag> oldPostTags = getPostTags(savedPost);

        List<String> hashTagList = Arrays.stream(hashTags.split("#"))
                .map(String::trim)
                .filter(x -> x.length() > 0)
                .collect(Collectors.toList());

        List<PostTag> needToDelete = new ArrayList<>();

        for (PostTag oldPostTag : oldPostTags) {
            boolean contains = hashTagList.stream().anyMatch(s -> s.equals(oldPostTag.getPostKeyword().getContent()));

            if (contains == false) {
                needToDelete.add(oldPostTag);
            }
        }

        needToDelete.forEach(postTag -> postTagRepository.delete(postTag));

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

    public List<PostTag> getPostTags(Post post) {
        return postTagRepository.findAllByPostId(post.getId());
    }

    public List<PostTag> getPostTagsByPostIdIn(long[] ids) {
        return postTagRepository.findAllByPostIdIn(ids);
    }

    public List<PostTag> getPostTags(Member member, String postKeywordContent) {
        return postTagRepository.findAllByMemberIdAndPostKeyword_contentOrderByPost_idDesc(member.getId(), postKeywordContent);
    }

    public List<PostTag> getPostTags(long authorId, long postKeywordId) {
        return postTagRepository.findAllByMemberIdAndPostKeywordIdOrderByPost_idDesc(authorId, postKeywordId);
    }

}
