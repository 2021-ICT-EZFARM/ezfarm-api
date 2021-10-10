package com.ezfarm.ezfarmback.iot.dto;

import com.ezfarm.ezfarmback.common.validator.ValueOfEnum;
import com.ezfarm.ezfarmback.iot.domain.OnOff;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RemoteRequest {

  @NotNull
  @ApiModelProperty(value = "Remote Index", required = true)
  private Long remoteId;

  @NotBlank
  @ValueOfEnum(enumClass = OnOff.class)
  @ApiModelProperty(value = "급수 제어", required = true)
  private String water;

  @NotBlank
  @ApiModelProperty(value = "온도 제어", required = true)
  private String temperature;

  @NotBlank
  @ValueOfEnum(enumClass = OnOff.class)
  @ApiModelProperty(value = "조도 제어", required = true)
  private String illuminance;

  @NotBlank
  @ValueOfEnum(enumClass = OnOff.class)
  @ApiModelProperty(value = "Co2 제어", required = true)
  private String co2;

  @Builder
  public RemoteRequest(Long remoteId, String water, String temperature, String illuminance,
      String co2) {
    this.remoteId = remoteId;
    this.water = water;
    this.temperature = temperature;
    this.illuminance = illuminance;
    this.co2 = co2;
  }
}
