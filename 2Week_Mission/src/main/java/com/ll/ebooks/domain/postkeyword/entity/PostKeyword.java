package com.ll.ebooks.domain.postkeyword.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class PostKeyword extends BaseEntity {

    private String content;

    @Builder.Default
    @Transient
    private Map<String, Object> extra = new LinkedHashMap<>();
    public long getExtra_postTagsCount() {
        return (long) getExtra().get("postTagsCount");
    }
}
