package com.ezfarm.ezfarmback.user.domain;

import com.ezfarm.ezfarmback.farm.domain.BestFarm;
import com.ezfarm.ezfarmback.farm.domain.MyFarm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserFarmHistory {

    @Id
    @GeneratedValue
    @Column(name = "user_farm_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

}
