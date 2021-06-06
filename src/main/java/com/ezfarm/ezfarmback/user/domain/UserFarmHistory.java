package com.ezfarm.ezfarmback.user.domain;

import com.ezfarm.ezfarmback.farm.domain.BestFarm;
import com.ezfarm.ezfarmback.farm.domain.MyFarm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserFarmHistory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private MyFarm myFarm;

    @ManyToOne
    private BestFarm bestFarm;
}
