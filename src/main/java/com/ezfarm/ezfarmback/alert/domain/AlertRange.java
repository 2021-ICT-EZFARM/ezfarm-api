package com.ezfarm.ezfarmback.alert.domain;

import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.common.domain.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AlertRange extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_range_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private float tmpMax;

    private float tmpMin;

    private float humidityMax;

    private float humidityMin;

    private float imnMax;

    private float imnMin;

    @Column(name = "co2_max")
    private float co2Max;

    @Column(name = "co2_min")
    private float co2Min;

    private float phMax;

    private float phMin;

    private float mosMax;

    private float mosMin;

    public AlertRange(Farm farm) {
        this.farm = farm;
    }

    public void updateAlertRange(AlertRangeRequest alertRangeRequest) {
        this.tmpMax = alertRangeRequest.getTmpMax();
        this.tmpMin = alertRangeRequest.getTmpMin();
        this.humidityMax = alertRangeRequest.getHumidityMax();
        this.humidityMin = alertRangeRequest.getHumidityMin();
        this.imnMax = alertRangeRequest.getImnMax();
        this.imnMin = alertRangeRequest.getImnMin();
        this.co2Max = alertRangeRequest.getCo2Max();
        this.co2Min = alertRangeRequest.getCo2Min();
        this.phMax = alertRangeRequest.getPhMax();
        this.phMin = alertRangeRequest.getPhMin();
        this.mosMax = alertRangeRequest.getMosMax();
        this.mosMin = alertRangeRequest.getMosMin();
    }
}
