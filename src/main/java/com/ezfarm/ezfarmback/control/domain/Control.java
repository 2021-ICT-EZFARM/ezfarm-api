package com.ezfarm.ezfarmback.control.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.MyFarm;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Setter
@Getter
@Entity
public class Control extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String key;

    private String value;

    private Boolean success;

    @ManyToOne(fetch = FetchType.LAZY)
    private MyFarm myFarm;

}
