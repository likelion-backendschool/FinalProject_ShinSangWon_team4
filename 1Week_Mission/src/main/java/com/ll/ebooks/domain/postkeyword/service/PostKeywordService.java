package com.ll.ebooks.domain.postkeyword.service;

import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import com.ll.ebooks.domain.postkeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostKeywordService {

    private final PostKeywordRepository postKeywordRepository;
    @Transactional
    public PostKeyword save(String content) {

        Optional<PostKeyword> opPostKeyword = postKeywordRepository.findByContent(content);

        if(opPostKeyword.isPresent()) {
            return opPostKeyword.get();
        }

        PostKeyword postKeyword = PostKeyword
                .builder()
                .content(content)
                .build();

        return postKeywordRepository.save(postKeyword);
    }
}
