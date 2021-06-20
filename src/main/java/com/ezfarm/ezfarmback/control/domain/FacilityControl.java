package com.ezfarm.ezfarmback.control.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.facility.domain.Facility;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class FacilityControl extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fc_ctr_id")
    private Long id;

    private FacilityType facilityType;

    private String ctrVal;

    private Boolean successYn;

}
