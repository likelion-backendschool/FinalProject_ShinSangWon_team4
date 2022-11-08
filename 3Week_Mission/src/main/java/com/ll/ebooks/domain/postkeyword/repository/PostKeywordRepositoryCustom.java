package com.ll.ebooks.domain.postkeyword.repository;

import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByMemberId(Long writerId);
}
