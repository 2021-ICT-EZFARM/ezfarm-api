package com.ezfarm.ezfarmback.facility.domain;

import javax.persistence.Embeddable;

@Embeddable
public class FacilityAvg {

    private float avg_tmp;

    private float avg_humidity;

    private float avg_illuminance;

    private float avg_co2;

    private float avg_ph;

    private float avg_mos;

}
