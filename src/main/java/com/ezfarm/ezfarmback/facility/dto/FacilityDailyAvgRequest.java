package com.ezfarm.ezfarmback.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDailyAvgRequest {

    private String year;

    private String month;
}
