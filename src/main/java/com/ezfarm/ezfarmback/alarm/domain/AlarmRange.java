package com.ezfarm.ezfarmback.alarm.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Setter
@Getter
@Entity
public class AlarmRange extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private int tmpMax;

    private int tmpMin;

    private int humidityMax;

    private int humidityMin;

    private int imnMax;

    private int imnMin;

    private int co2Max;

    private int co2Min;

    private int phMax;

    private int phMin;

    private int mosMax;

    private int mosMin;

}
