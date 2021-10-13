package com.ezfarm.ezfarmback.facility.domain.hour;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private float tmp;

    private float humidity;

    private float illuminance;

    private float co2;

    private float ph;

    private float mos;

    private LocalDateTime measureDate;

    @Builder
    public Facility(Farm farm, float humidity, float tmp, float illuminance, float co2,
        float ph, float mos, LocalDateTime measureDate) {
        this.farm = farm;
        this.humidity = humidity;
        this.tmp = tmp;
        this.illuminance = illuminance;
        this.co2 = co2;
        this.ph = ph;
        this.mos = mos;
        this.measureDate = measureDate;
    }

}
