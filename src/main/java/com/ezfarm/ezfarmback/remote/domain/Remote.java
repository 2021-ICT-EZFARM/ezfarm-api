package com.ezfarm.ezfarmback.remote.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Remote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fc_ctr_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private String values;

    private Boolean successYn;

    @Builder
    public Remote(Farm farm, String values, Boolean successYn) {
        this.farm = farm;
        this.values = values;
        this.successYn = successYn;
    }

}
