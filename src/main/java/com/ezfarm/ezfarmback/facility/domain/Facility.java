package com.ezfarm.ezfarmback.facility.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
public class Facility extends BaseTimeEntity {
    @Id
    @GeneratedValue
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
}
