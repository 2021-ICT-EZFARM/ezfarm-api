package com.ezfarm.ezfarmback.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    @ApiModelProperty(value = "페이지(0부터 시작)", required = true)
    private int page;

    @ApiModelProperty(value = "사이즈(한 페이지 내의 콘텐츠 수)", required = true)
    private int size;
}
