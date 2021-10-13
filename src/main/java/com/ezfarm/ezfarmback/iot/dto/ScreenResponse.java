package com.ezfarm.ezfarmback.iot.dto;

import com.ezfarm.ezfarmback.iot.domain.Screen;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ScreenResponse {

  @ApiModelProperty(value = "이미지 url")
  private String imageUrl;

  @ApiModelProperty(value = "생장률")
  private String cropCondition;

  @ApiModelProperty(value = "측정 시간(1~24)")
  private String measureTime;

  @Builder
  public ScreenResponse(String imageUrl, String cropCondition, String measureTime) {
    this.imageUrl = imageUrl;
    this.cropCondition = cropCondition;
    this.measureTime = measureTime;
  }

  public static ScreenResponse of(Screen screen) {
    return ScreenResponse.builder()
        .imageUrl(screen.getImageUrl())
        .cropCondition(screen.getCropCondition())
        .measureTime(screen.getMeasureTime())
        .build();
  }
}
