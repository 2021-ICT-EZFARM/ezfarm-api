package com.ezfarm.ezfarmback.farm.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class FarmInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private float tmp;

    private float humidity;

    private float illuminance;

    private float co2;

    private float ph;

    private float mos;

    @ManyToOne
    private MyFarm myFarm;
}
