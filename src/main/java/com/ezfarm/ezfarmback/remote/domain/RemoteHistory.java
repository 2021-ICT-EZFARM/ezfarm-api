package com.ezfarm.ezfarmback.remote.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RemoteHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fc_ctr_hstr_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private String values;

    private Boolean successYn;
}
