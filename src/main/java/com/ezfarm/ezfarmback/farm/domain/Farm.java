package com.ezfarm.ezfarmback.farm.domain;


import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Farm extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "farm_id")
    private Long id;

    private String address;

    private String phoneNumber;

    private String area;

    private Boolean isMain;

    @Enumerated(value = EnumType.STRING)
    private FarmType farmType;

    @Enumerated(value = EnumType.STRING)
    private CropType cropType;

    private LocalDateTime startDate;
}
