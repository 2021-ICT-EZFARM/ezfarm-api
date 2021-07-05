package com.ezfarm.ezfarmback.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertRangeResponse {

    private Long id;
    private float tmpMax;
    private float tmpMin;
    private float humidityMax;
    private float humidityMin;
    private float imnMax;
    private float imnMin;
    private float co2Max;
    private float co2Min;
    private float phMax;
    private float phMin;
    private float mosMax;
    private float mosMin;

}
