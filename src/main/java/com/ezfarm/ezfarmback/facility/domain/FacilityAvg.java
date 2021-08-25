package com.ezfarm.ezfarmback.facility.domain;

import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgSearchResponse;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class FacilityAvg {

    private float avgTmp;

    private float avgHumidity;

    private float avgIlluminance;

    private float avgCo2;

    private float avgPh;

    private float avgMos;

    public FacilityAvg() {
        this.avgTmp = 0;
        this.avgHumidity = 0;
        this.avgIlluminance = 0;
        this.avgCo2 = 0;
        this.avgPh = 0;
        this.avgMos = 0;
    }

    public FacilityAvg(FacilityAvgSearchResponse facilityAvgSearchResponse) {
        this.avgTmp = facilityAvgSearchResponse.getAvgTmp();
        this.avgHumidity = facilityAvgSearchResponse.getAvgHumidity();
        this.avgIlluminance = facilityAvgSearchResponse.getAvgIlluminance();
        this.avgCo2 = facilityAvgSearchResponse.getAvgCo2();
        this.avgPh = facilityAvgSearchResponse.getAvgPh();
        this.avgMos = facilityAvgSearchResponse.getAvgMos();
    }

    public void sum(Facility facility) {
        this.avgTmp += facility.getTmp();
        this.avgHumidity += facility.getHumidity();
        this.avgIlluminance += facility.getIlluminance();
        this.avgCo2 += facility.getCo2();
        this.avgPh += facility.getPh();
        this.avgMos += facility.getMos();
    }

    public void sum(FacilityAvg facilityAvg) {
        this.avgTmp += facilityAvg.avgTmp;
        this.avgHumidity += facilityAvg.avgHumidity;
        this.avgIlluminance += facilityAvg.avgIlluminance;
        this.avgCo2 += facilityAvg.avgCo2;
        this.avgPh += facilityAvg.avgPh;
        this.avgMos += facilityAvg.avgMos;
    }

    public void average(int hours) {
        if (hours != 0) {
            this.avgTmp /= hours;
            this.avgHumidity /= hours;
            this.avgIlluminance /= hours;
            this.avgCo2 /= hours;
            this.avgPh /= hours;
            this.avgMos /= hours;
        }
    }
}
