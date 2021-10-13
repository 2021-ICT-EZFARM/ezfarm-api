package com.ezfarm.ezfarmback.alert.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertRangeRequest {

  @NotNull
  @ApiModelProperty(required = true)
  private float tmpMax;

  @NotNull
  @ApiModelProperty(required = true)
  private float tmpMin;

  @NotNull
  @ApiModelProperty(required = true)
  private float humidityMax;

  @NotNull
  @ApiModelProperty(required = true)
  private float humidityMin;

  @NotNull
  @ApiModelProperty(value = "최대 조도", required = true)
  private float imnMax;

  @NotNull
  @ApiModelProperty(value = "최소 조도", required = true)
  private float imnMin;

  @NotNull
  @ApiModelProperty(required = true)
  private float co2Max;

  @NotNull
  @ApiModelProperty(required = true)
  private float co2Min;

  @NotNull
  @ApiModelProperty(required = true)
  private float phMax;

  @NotNull
  @ApiModelProperty(required = true)
  private float phMin;

  @NotNull
  @ApiModelProperty(value = "최대 토양 수분", required = true)
  private float mosMax;

  @NotNull
  @ApiModelProperty(value = "최소 토양 수분", required = true)
  private float mosMin;
}
