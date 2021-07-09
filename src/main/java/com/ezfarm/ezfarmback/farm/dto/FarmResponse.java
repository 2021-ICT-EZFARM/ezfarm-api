package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import java.time.LocalDate;
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

    @Builder
    public FarmResponse(Long id, String address, String name, String phoneNumber, String area,
        boolean isMain, FarmType farmType, CropType cropType, LocalDate startDate) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.area = area;
        this.isMain = isMain;
        this.farmType = farmType;
        this.cropType = cropType;
        this.startDate = startDate;
    }

    public static FarmResponse of(Farm farm) {
        return new FarmResponse(
            farm.getId(),
            farm.getAddress(),
            farm.getName(),
            farm.getPhoneNumber(),
            farm.getArea(),
            farm.isMain(),
            farm.getFarmType(),
            farm.getCropType(),
            farm.getStartDate()
        );
    }

}
