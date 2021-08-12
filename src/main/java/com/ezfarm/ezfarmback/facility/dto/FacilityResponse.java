package com.ezfarm.ezfarmback.facility.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacilityResponse {

    private String tmp;

    private String humidity;

    private String illuminance;

    private String co2;

    private String ph;

    private String mos;

    private String measureDate;

    @Builder
    public FacilityResponse(String tmp, String humidity, String illuminance, String co2,
        String ph, String mos, String measureDate) {
        this.tmp = tmp;
        this.humidity = humidity;
        this.illuminance = illuminance;
        this.co2 = co2;
        this.ph = ph;
        this.mos = mos;
        this.measureDate = measureDate;
    }

    public static FacilityResponse stringParseToFacilityRes(String output) {
        String[] split = output.split(",");
        return FacilityResponse.builder()
            .humidity(split[0].trim())
            .tmp(split[1].trim())
            .illuminance(split[2].trim())
            .co2(split[3].trim())
            .ph(split[4].trim())
            .mos(split[5].trim())
            .measureDate(split[6].trim())
            .build();
    }
}
