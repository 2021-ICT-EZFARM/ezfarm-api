package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmResponse {

    private Long id;

    private String address;

    private String name;

    private String phoneNumber;

    private String area;

    private boolean isMain;

    private FarmType farmType;

    private CropType cropType;

    private LocalDate startDate;

    private LocalDateTime createdDate;

    @Builder
    public FarmResponse(Long id, String address, String name, String phoneNumber, String area,
        boolean isMain, FarmType farmType, CropType cropType, LocalDate startDate,
        LocalDateTime createdDate) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.isMain = isMain;
        this.farmType = farmType;
        this.cropType = cropType;
        this.startDate = startDate;
        this.createdDate = createdDate;
    }

    public static FarmResponse of(Farm farm) {
        return FarmResponse.builder()
            .id(farm.getId())
            .name(farm.getName())
            .address(farm.getAddress())
            .phoneNumber(farm.getPhoneNumber())
            .area(farm.getArea())
            .startDate(farm.getStartDate())
            .farmType(farm.getFarmType())
            .cropType(farm.getCropType())
            .createdDate(farm.getCreatedDate())
            .build();
    }

}
