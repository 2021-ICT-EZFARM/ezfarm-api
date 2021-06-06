package com.ezfarm.ezfarmback.farm.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class Farm {

    @Id
    @GeneratedValue
    private Long id;

    private String address;

    @Enumerated(value = EnumType.STRING)
    private FarmType farmType;

    @Enumerated(value = EnumType.STRING)
    private Crop crop;

    @Column(nullable = false)
    private String area;

}
