package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmSearchResponse {

  private Long farmId;
  private String name;
  private String address;
  private String area;
  private String farmType;
  private String cropType;

  @QueryProjection
  public FarmSearchResponse(Long farmId, String name, String address, String area,
      FarmType farmType, CropType cropType) {
    this.farmId = farmId;
    this.name = name;
    this.address = address;
    this.area = area;
    this.farmType = farmType.getName();
    this.cropType = cropType.getName();
  }

  public static FarmSearchResponse favoriteOf(Favorite favorite) {
    return new FarmSearchResponse(
        favorite.getFarm().getId(),
        favorite.getFarm().getName(),
        favorite.getFarm().getAddress(),
        favorite.getFarm().getArea(),
        favorite.getFarm().getFarmType(),
        favorite.getFarm().getCropType()
    );
  }
}
