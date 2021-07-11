package com.ezfarm.ezfarmback.remote.dto;

import com.ezfarm.ezfarmback.remote.domain.OnOff;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RemoteRequest {

    @NotNull
    @ApiModelProperty(value = "Remote Index", required = true)
    private Long remoteId;

    @NotNull
    @ApiModelProperty(value = "급수 제어", required = true)
    private OnOff water;

    @NotNull
    @ApiModelProperty(value = "온도 제어", required = true)
    private float temperature;

    @NotNull
    @ApiModelProperty(value = "조도 제어", required = true)
    private OnOff illuminance;

    @NotNull
    @ApiModelProperty(value = "Co2 제어", required = true)
    private OnOff co2;
}
