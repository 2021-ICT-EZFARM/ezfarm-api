package com.ezfarm.ezfarmback.alert.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AlertRange extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "alert_range_id")
    private Long id;

    private int tmpMax;

    private int tmpMin;

    private int humidityMax;

    private int humidityMin;

    private int imnMax;

    private int imnMin;

    @Column(name = "co2_max")
    private int co2Max;

    @Column(name = "co2_min")
    private int co2Min;

    private int phMax;

    private int phMin;

    private int mosMax;

    private int mosMin;

}
