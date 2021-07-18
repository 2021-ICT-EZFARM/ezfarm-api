package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmSearchCond {

    private FarmType farmType;
    private CropType cropType;
}
