package com.ezfarm.ezfarmback.remote.dto;

import com.ezfarm.ezfarmback.remote.domain.OnOff;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RemoteRequest {

    @ApiModelProperty(value = "Remote Index", required = true)
    private Long remoteId;

    @ApiModelProperty(value = "급수 제어", required = true)
    private OnOff water;

    @ApiModelProperty(value = "온도 제어", required = true)
    private float temperature;

    @ApiModelProperty(value = "조도 제어", required = true)
    private OnOff illuminance;

    @ApiModelProperty(value = "Co2 제어", required = true)
    private OnOff co2;
}
