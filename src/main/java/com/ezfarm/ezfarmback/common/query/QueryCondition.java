package com.ezfarm.ezfarmback.common.query;

import static com.ezfarm.ezfarmback.farm.domain.QFarm.farm;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.querydsl.core.types.dsl.BooleanExpression;

public class QueryCondition {

    public static BooleanExpression farmTypeEq(FarmType farmType) {
        return farmType != null ? farm.farmType.eq(farmType) : null;
    }

    public static BooleanExpression cropTypeEq(CropType cropType) {
        return cropType != null ? farm.cropType.eq(cropType) : null;
    }
}