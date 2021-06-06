package com.ezfarm.ezfarmback.farm.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestFarm extends Farm{

    @Builder
    public BestFarm(Long id, String address, FarmType farmType, Crop crop, String area) {
        super(id, address, farmType, crop, area);
    }

}
