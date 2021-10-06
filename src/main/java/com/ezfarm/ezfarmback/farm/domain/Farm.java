package com.ezfarm.ezfarmback.farm.domain;


import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
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
  public Farm(Long id, String address, String name, String phoneNumber, String area, boolean isMain,
      FarmType farmType, CropType cropType, LocalDate startDate, User user, FarmGroup farmGroup) {
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

  public void setMain(boolean isMain) {
    this.isMain = isMain;
  }

  public static Farm create(User loginUser, FarmRequest request) {
    return Farm.builder()
        .user(loginUser)
        .name(request.getName())
        .address(request.getAddress())
        .phoneNumber(request.getPhoneNumber())
        .area(request.getArea())
        .isMain(request.getIsMain())
        .farmType(FarmType.valueOf(request.getFarmType()))
        .cropType(CropType.valueOf(request.getCropType()))
        .farmGroup(FarmGroup.NORMAL)
        .startDate(request.getStartDate())
        .build();
  }

  public void update(FarmRequest request) {
    this.address = request.getAddress();
    this.name = request.getName();
    this.phoneNumber = request.getPhoneNumber();
    this.area = request.getArea();
    this.isMain = request.getIsMain();
    this.farmType = FarmType.valueOf(request.getFarmType());
    this.cropType = CropType.valueOf(request.getCropType());
    this.startDate = request.getStartDate();
  }

  public void validateIsMyFarm(User loginUser) {
    if (!this.user.getId().equals(loginUser.getId())) {
      throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
    }
  }

  public boolean isSameFarm(Long farmId) {
    return this.getId().equals(farmId);
  }
}
