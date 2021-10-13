package com.ezfarm.ezfarmback.screen.dto;

import com.ezfarm.ezfarmback.screen.domain.Screen;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScreenResponse {

    @ApiModelProperty(value = "이미지 url")
    private String imageUrl;

    @ApiModelProperty(value = "생장률")
    private float cropCondition;

    @ApiModelProperty(value = "측정 시간(1~24)")
    private int measureTime;

    public static ScreenResponse of(Screen screen) {
        return new ScreenResponse(
            screen.getImageUrl(),
            screen.getCropCondition(),
            screen.getMeasureTime()
        );
    }
}
