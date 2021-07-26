package com.ezfarm.ezfarmback.facility.domain;

import javax.persistence.Embeddable;

@Embeddable
public class FacilityAvg{

    private float avg_tmp;

    private float avg_humidity;

    private float avg_illuminance;

    private float avg_co2;

    private float avg_ph;

    private float avg_mos;

    public FacilityAvg(){
        this.avg_tmp = 0;
        this.avg_humidity = 0;
        this.avg_illuminance = 0;
        this.avg_co2 = 0;
        this.avg_ph = 0;
        this.avg_mos = 0;
    }

    public void sum(Facility facility) {
        this.avg_tmp += facility.getTmp();
        this.avg_humidity += facility.getHumidity();
        this.avg_illuminance += facility.getIlluminance();
        this.avg_co2 += facility.getCo2();
        this.avg_ph += facility.getPh();
        this.avg_mos += facility.getMos();
    }

    public void sum(FacilityAvg facilityAvg) {
        this.avg_tmp += facilityAvg.avg_tmp;
        this.avg_humidity += facilityAvg.avg_humidity;
        this.avg_illuminance += facilityAvg.avg_illuminance;
        this.avg_co2 += facilityAvg.avg_co2;
        this.avg_ph += facilityAvg.avg_ph;
        this.avg_mos += facilityAvg.avg_mos;
    }

    public void average(int hours) {
        if (hours != 0) {
            this.avg_tmp /= hours;
            this.avg_humidity /= hours;
            this.avg_illuminance /= hours;
            this.avg_co2 /= hours;
            this.avg_ph /= hours;
            this.avg_mos /= hours;
        }
    }
}
