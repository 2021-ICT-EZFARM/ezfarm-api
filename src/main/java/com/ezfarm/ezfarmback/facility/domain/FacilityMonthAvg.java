package com.ezfarm.ezfarmback.facility.domain;


import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    private String measureDate;

    @Builder
    public FacilityMonthAvg(Farm farm, FacilityAvg facilityAvg, LocalDateTime measureDate) {
        this.farm = farm;
        this.facilityAvg = facilityAvg;
        this.measureDate = measureDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
