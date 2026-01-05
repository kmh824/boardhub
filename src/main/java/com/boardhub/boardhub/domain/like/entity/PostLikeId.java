package com.boardhub.boardhub.domain.like.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // 복합키 필수: 두 키 객체가 같은지 비교하기 위해 필요
public class PostLikeId implements Serializable {
    private Long member; // PostLike 엔티티의 변수명과 일치해야 함
    private Long post;   // PostLike 엔티티의 변수명과 일치해야 함
}