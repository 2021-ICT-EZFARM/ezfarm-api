package com.ezfarm.ezfarmback.farm.dto.detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthCond {

    private String year;
    private String startMonth;
    private String endMonth;
}
