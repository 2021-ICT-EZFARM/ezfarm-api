package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmSearchCond {

    private FarmGroup farmGroup;
    private FarmType farmType;
    private CropType cropType;

    @Builder
    public FarmSearchCond(FarmGroup farmGroup,
        FarmType farmType, CropType cropType) {
        this.farmGroup = farmGroup;
        this.farmType = farmType;
        this.cropType = cropType;
    }
}
