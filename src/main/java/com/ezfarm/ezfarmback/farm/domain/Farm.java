package com.ezfarm.ezfarmback.farm.domain;


import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Farm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String address;

    private String phoneNumber;

    private String area;

    private boolean isMain;

    @Enumerated(value = EnumType.STRING)
    private FarmType farmType;

    @Enumerated(value = EnumType.STRING)
    private CropType cropType;

    @Enumerated(value = EnumType.STRING)
    private FarmGroup farmGroup;

    private LocalDate startDate;

    @Builder
    public Farm(Long id, String address, String name, String phoneNumber, String area,
        boolean isMain,
        FarmType farmType,
        CropType cropType, LocalDate startDate, User user, FarmGroup farmGroup) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.isMain = isMain;
        this.farmType = farmType;
        this.cropType = cropType;
        this.startDate = startDate;
        this.user = user;
        this.farmGroup = farmGroup;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFarmGroup(FarmGroup farmGroup) {
        this.farmGroup = farmGroup;
    }

    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }

    public void update(FarmRequest farmRequest) {
        this.address = farmRequest.getAddress();
        this.name = farmRequest.getName();
        this.phoneNumber = farmRequest.getPhoneNumber();
        this.area = farmRequest.getArea();
        this.isMain = farmRequest.isMain();
        this.farmType = farmRequest.getFarmType();
        this.cropType = farmRequest.getCropType();
        this.startDate = farmRequest.getStartDate();
    }

    public boolean isMyFarm(Long userId) {
        return this.getUser().getId().equals(userId);
    }

    public boolean isSameFarm(Long farmId) {
        return this.getId().equals(farmId);
    }
}
