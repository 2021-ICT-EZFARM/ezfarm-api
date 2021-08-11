package com.ezfarm.ezfarmback.facility.dto;

import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacilityAvgResponse {

    private float avgTmp;

    private float avgHumidity;

    private float avgIlluminance;

    private float avgCo2;

    private float avgPh;

    private float avgMos;

    private String measureDate;

    @Builder
    public FacilityAvgResponse(float avgTmp, float avgHumidity, float avgIlluminance, float avgCo2,
        float avgPh, float avgMos, String measureDate) {
        this.avgTmp = avgTmp;
        this.avgHumidity = avgHumidity;
        this.avgIlluminance = avgIlluminance;
        this.avgCo2 = avgCo2;
        this.avgPh = avgPh;
        this.avgMos = avgMos;
        this.measureDate = measureDate;
    }

    public static List<FacilityAvgResponse> listOfDayAvg(List<FacilityDayAvg> dayAvgs) {
        return dayAvgs.stream().map(v -> FacilityAvgResponse.builder()
            .avgTmp(v.getFacilityAvg().getAvgTmp())
            .avgCo2(v.getFacilityAvg().getAvgCo2())
            .avgHumidity(v.getFacilityAvg().getAvgHumidity())
            .avgIlluminance(v.getFacilityAvg().getAvgIlluminance())
            .avgMos(v.getFacilityAvg().getAvgMos())
            .avgPh(v.getFacilityAvg().getAvgPh())
            .measureDate(v.getMeasureDate())
            .build()
        ).collect(Collectors.toList());
    }

    public static List<FacilityAvgResponse> listOfMonthAvg(List<FacilityMonthAvg> monthAvgs) {
        return monthAvgs.stream().map(v -> FacilityAvgResponse.builder()
            .avgTmp(v.getFacilityAvg().getAvgTmp())
            .avgCo2(v.getFacilityAvg().getAvgCo2())
            .avgHumidity(v.getFacilityAvg().getAvgHumidity())
            .avgIlluminance(v.getFacilityAvg().getAvgIlluminance())
            .avgMos(v.getFacilityAvg().getAvgMos())
            .avgPh(v.getFacilityAvg().getAvgPh())
            .measureDate(v.getMeasureDate())
            .build()
        ).collect(Collectors.toList());
    }

    public static List<FacilityAvgResponse> listOfWeekAvg(List<FacilityWeekAvg> weekAvgs) {
        return weekAvgs.stream().map(v -> FacilityAvgResponse.builder()
            .avgTmp(v.getFacilityAvg().getAvgTmp())
            .avgCo2(v.getFacilityAvg().getAvgCo2())
            .avgHumidity(v.getFacilityAvg().getAvgHumidity())
            .avgIlluminance(v.getFacilityAvg().getAvgIlluminance())
            .avgMos(v.getFacilityAvg().getAvgMos())
            .avgPh(v.getFacilityAvg().getAvgPh())
            .measureDate(v.getMeasureDate())
            .build()
        ).collect(Collectors.toList());
    }
}
