package com.ezfarm.ezfarmback.domain.farm;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.domain.farm.enums.CropType;
import com.ezfarm.ezfarmback.domain.farm.enums.FarmKind;
import com.ezfarm.ezfarmback.domain.farm.enums.FarmType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farm_id")
    private Long id;

    private String name;

    private String address;

    private String tel;

    private String area;

    @Enumerated(EnumType.STRING)
    private FarmKind kind;

    @Enumerated(EnumType.STRING)
    private FarmType farmType;

    @Enumerated(EnumType.STRING)
    private CropType cropType;

    private LocalDateTime startDate;

}
