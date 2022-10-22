package com.ll.ebooks.domain.postkeyword.entity;

import com.ll.ebooks.domain.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class PostKeyword extends BaseEntity {

    private String content;

}
