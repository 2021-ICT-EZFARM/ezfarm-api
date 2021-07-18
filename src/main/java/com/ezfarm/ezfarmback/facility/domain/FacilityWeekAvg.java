package com.ezfarm.ezfarmback.facility.domain;


import com.ezfarm.ezfarmback.farm.domain.Farm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
public class FacilityWeekAvg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Embedded
    private FacilityAvg facilityAvg;
}
