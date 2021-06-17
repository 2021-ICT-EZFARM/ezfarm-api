package com.ezfarm.ezfarmback.farm.domain;


import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmForm;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.*;

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

    private Boolean isMain = false;

    @Enumerated(value = EnumType.STRING)
    private FarmType farmType;

    @Enumerated(value = EnumType.STRING)
    private CropType cropType;

    private LocalDateTime startDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Farm(String address, String phoneNumber, String area, Boolean isMain, FarmType farmType,
                CropType cropType, LocalDateTime startDate, User user) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.isMain = isMain;
        this.farmType = farmType;
        this.cropType = cropType;
        this.startDate = startDate;
        this.user = user;
    }

    public void addOwner(User user) {
        this.user = user;
    }

    public void update(FarmForm farmForm) {
        this.address = farmForm.getAddress();
        this.phoneNumber = farmForm.getPhoneNumber();
        this.area = farmForm.getArea();
        this.farmType = farmForm.getFarmType();
        this.cropType = farmForm.getCropType();
        this.startDate = farmForm.getStartDate();
    }

    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }
}
