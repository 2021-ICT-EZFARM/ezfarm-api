package com.ezfarm.ezfarmback.alert.dto;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertRangeResponse {

  private Long id;
  private float tmpMax;
  private float tmpMin;
  private float humidityMax;
  private float humidityMin;
  @ApiModelProperty(value = "최대 조도")
  private float imnMax;
  @ApiModelProperty(value = "최소 조도")
  private float imnMin;
  private float co2Max;
  private float co2Min;
  private float phMax;
  private float phMin;
  @ApiModelProperty(value = "최대 토양 수분")
  private float mosMax;
  @ApiModelProperty(value = "최소 토양 수분")
  private float mosMin;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
}
