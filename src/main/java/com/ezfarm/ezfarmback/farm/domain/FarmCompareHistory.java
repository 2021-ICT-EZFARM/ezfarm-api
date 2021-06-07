package com.ezfarm.ezfarmback.farm.domain;

import com.ezfarm.ezfarmback.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class FarmCompareHistory {

    @Id
    @GeneratedValue
    @Column(name = "fm_cp_hstr_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

}
