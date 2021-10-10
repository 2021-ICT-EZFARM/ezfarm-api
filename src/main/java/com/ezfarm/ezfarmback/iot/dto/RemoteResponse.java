package com.ezfarm.ezfarmback.iot.dto;

import com.ezfarm.ezfarmback.iot.domain.OnOff;
import com.ezfarm.ezfarmback.iot.domain.Remote;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RemoteResponse {

  private Long id;
  private OnOff water;
  private String temperature;
  private OnOff illuminance;
  private OnOff co2;

  @Builder
  public RemoteResponse(Long id, OnOff water, String temperature, OnOff illuminance, OnOff co2) {
    this.id = id;
    this.water = water;
    this.temperature = temperature;
    this.illuminance = illuminance;
    this.co2 = co2;
  }

  public static RemoteResponse of(Remote remote) {
    return RemoteResponse.builder()
        .id(remote.getId())
        .water(remote.getWater())
        .temperature(remote.getTemperature())
        .illuminance(remote.getIlluminance())
        .co2(remote.getCo2())
        .build();
  }
}
