package com.ezfarm.ezfarmback.facility.domain;


import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
public class FacilityMonthAvg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private String measureDate;

    @Embedded
    private FacilityAvg facilityAvg;

    @Builder
    public FacilityMonthAvg(Farm farm, FacilityAvg facilityAvg, LocalDateTime measureDate) {
        this.farm = farm;
        this.facilityAvg = facilityAvg;
        this.measureDate = measureDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
