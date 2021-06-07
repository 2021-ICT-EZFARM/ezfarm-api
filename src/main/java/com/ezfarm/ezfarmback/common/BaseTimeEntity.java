package com.ezfarm.ezfarmback.common;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
