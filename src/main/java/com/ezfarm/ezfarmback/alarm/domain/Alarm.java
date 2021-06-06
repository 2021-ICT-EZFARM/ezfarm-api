package com.ezfarm.ezfarmback.alarm.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Entity
public class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String key;

    private AlarmType alarmType;

    private Boolean isChecked;

    @Builder
    public Alarm(Long id, String key, AlarmType alarmType, Boolean isChecked) {
        this.id = id;
        this.key = key;
        this.alarmType = alarmType;
        this.isChecked = isChecked;
    }

}
