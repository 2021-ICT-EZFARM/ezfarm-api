package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.common.validator.ValueOfEnum;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmSearchCond {

  @ValueOfEnum(enumClass = FarmGroup.class)
  private String farmGroup;

  @ValueOfEnum(enumClass = FarmType.class)
  private String farmType;
  
  @ValueOfEnum(enumClass = CropType.class)
  private String cropType;

  @Builder
  public FarmSearchCond(String farmGroup, String farmType, String cropType) {
    this.farmGroup = farmGroup;
    this.farmType = farmType;
    this.cropType = cropType;
  }
}
