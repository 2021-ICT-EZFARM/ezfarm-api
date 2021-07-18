package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmSearchResponse {

    private Long farmId;
    private String name;
    private String address;
    private String area;
    private String phoneNumber;
    private FarmType farmType;
    private CropType cropType;
    private LocalDate startDate;

    @QueryProjection
    public FarmSearchResponse(Long farmId, String name, String address, String area,
        String phoneNumber, FarmType farmType, CropType cropType, LocalDate startDate) {
        this.farmId = farmId;
        this.name = name;
        this.address = address;
        this.area = area;
        this.phoneNumber = phoneNumber;
        this.farmType = farmType;
        this.cropType = cropType;
        this.startDate = startDate;
    }
}
