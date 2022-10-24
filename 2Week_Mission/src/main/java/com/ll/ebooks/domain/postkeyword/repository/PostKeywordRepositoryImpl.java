package com.ll.ebooks.domain.postkeyword.repository;

import com.ll.ebooks.domain.postkeyword.entity.PostKeyword;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.ll.ebooks.domain.postkeyword.entity.QPostKeyword.postKeyword;
import static com.ll.ebooks.domain.posttag.entity.QPostTag.postTag;

@RequiredArgsConstructor
public class PostKeywordRepositoryImpl implements PostKeywordRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostKeyword> getQslAllByMemberId(Long memberId) {
        List<Tuple> fetch = jpaQueryFactory
                .select(postKeyword, postTag.count())
                .from(postKeyword)
                .innerJoin(postTag)
                .on(postKeyword.eq(postTag.postKeyword))
                .where(postTag.member.id.eq(memberId))
                .orderBy(postTag.post.id.desc())
                .groupBy(postKeyword.id)
                .fetch();

        return fetch.stream().
                map(tuple -> {
                    PostKeyword _postKeyword = tuple.get(postKeyword);
                    Long postTagsCount = tuple.get(postTag.count());

                    _postKeyword.getExtra().put("postTagsCount", postTagsCount);

                    return _postKeyword;
                })
                .collect(Collectors.toList());
    }
}
