package com.ezfarm.ezfarmback.facility.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDailyAvgRequest {

    @NotBlank
    @ApiModelProperty(example = "2021", required = true)
    private String year;

    @NotBlank
    @ApiModelProperty(example = "01", required = true)
    private String month;
}
