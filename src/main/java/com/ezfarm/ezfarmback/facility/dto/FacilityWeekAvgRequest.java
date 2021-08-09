package com.ezfarm.ezfarmback.facility.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityWeekAvgRequest {

    @NotBlank
    @ApiModelProperty(example = "2021-01")
    private String dateOne;

    @NotBlank
    @ApiModelProperty(example = "2021-02")
    private String dateTwo;

    @NotBlank
    @ApiModelProperty(example = "2021-03")
    private String dateThr;

}
