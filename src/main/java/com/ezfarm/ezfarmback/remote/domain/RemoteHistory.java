package com.ezfarm.ezfarmback.remote.domain;

import com.ezfarm.ezfarmback.common.domain.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RemoteHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "remote_history_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "farm_id")
  private Farm farm;

  private OnOff water;

  private float temperature;

  private OnOff illuminance;

  private OnOff co2;

  private Boolean successYn;

  public static RemoteHistory of(Farm farm, RemoteRequest remoteRequest) {
    return RemoteHistory.builder()
        .farm(farm)
        .water(remoteRequest.getWater())
        .temperature(remoteRequest.getTemperature())
        .illuminance(remoteRequest.getIlluminance())
        .co2(remoteRequest.getCo2())
        .build();
  }

  @Builder
  public RemoteHistory(Farm farm, OnOff water, float temperature, OnOff illuminance, OnOff co2) {
    this.farm = farm;
    this.water = water;
    this.temperature = temperature;
    this.illuminance = illuminance;
    this.co2 = co2;
  }

    public void setSuccessYn(boolean successYn) {
        this.successYn = successYn;
    }
}
