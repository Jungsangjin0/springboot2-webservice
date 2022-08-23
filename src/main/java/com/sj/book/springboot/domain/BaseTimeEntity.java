package com.sj.book.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //JPA Entity클래스들이 상속할 경우 필드들(createDate, modifiedMDate)도 칼럼으로 인식하도록 합니다.
@EntityListeners(AuditingEntityListener.class) //auditing 기능을 포함시킵니다.
public class BaseTimeEntity {

    @CreatedDate //entity가 생성되어 저장될 때 시간이 자동 저장됩니다.
    private LocalDateTime createdDate;

    @LastModifiedDate //entity값을 변경할 때 자동 저장됩니다.
    private LocalDateTime modifiedDate;

}
