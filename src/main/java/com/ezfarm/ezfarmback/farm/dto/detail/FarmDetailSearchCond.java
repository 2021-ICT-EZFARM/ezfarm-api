package com.ezfarm.ezfarmback.farm.dto.detail;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmDetailSearchCond {

    private boolean isDefault;

    private PeriodType periodType;

    private DayCond dayCond;

    private MonthCond monthCond;

    private YearCond yearCond;

    @Builder
    public FarmDetailSearchCond(boolean isDefault,
        PeriodType periodType, DayCond dayCond,
        MonthCond monthCond, YearCond yearCond) {
        this.isDefault = isDefault;
        this.periodType = periodType;
        this.dayCond = dayCond;
        this.monthCond = monthCond;
        this.yearCond = yearCond;
    }
}
