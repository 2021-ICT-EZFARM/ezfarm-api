package com.ezfarm.ezfarmback.remote.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Remote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "remote_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private OnOff water;

    private float temperature;

    private OnOff illuminance;

    private OnOff co2;

    @Builder
    public Remote(Long id, Farm farm, OnOff water, float temperature,
        OnOff illuminance, OnOff co2) {
        this.id = id;
        this.farm = farm;
        this.water = water;
        this.temperature = temperature;
        this.illuminance = illuminance;
        this.co2 = co2;
    }

    public void updateRemote(RemoteRequest remoteRequest) {
        this.water = remoteRequest.getWater();
        this.temperature = remoteRequest.getTemperature();
        this.illuminance = remoteRequest.getIlluminance();
        this.co2 = remoteRequest.getCo2();
    }
}
