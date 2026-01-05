package com.boardhub.boardhub.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속받는 엔티티들이 아래 필드들을 컬럼으로 인식하게 함
@EntityListeners(AuditingEntityListener.class) // Auditing(자동 값 매핑) 기능 활성화
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false) // 생성 시간은 변경되지 않음
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}