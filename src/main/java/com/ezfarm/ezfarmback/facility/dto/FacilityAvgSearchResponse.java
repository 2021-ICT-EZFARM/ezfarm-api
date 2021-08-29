package com.ezfarm.ezfarmback.facility.dto;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacilityAvgSearchResponse {

  private Farm farm;

  private float avgTmp;

  private float avgHumidity;

  private float avgIlluminance;

  private float avgCo2;

  private float avgPh;

  private float avgMos;

  private String measureDate;

  public void setMeasureDate(String measureDate) {
    this.measureDate = measureDate;
  }

  @QueryProjection
  public FacilityAvgSearchResponse(Farm farm, float tmp, float humidity, float illuminance, float co2, float ph, float mos) {
    this.farm = farm;
    this.avgTmp = tmp;
    this.avgHumidity = humidity;
    this.avgIlluminance = illuminance;
    this.avgCo2 = co2;
    this.avgPh = ph;
    this.avgMos = mos;
  }
}
