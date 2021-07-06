package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FarmResponse {

  private String address;

    private Long id;

    private String address;

    private String address;

    private String name;

    private String phoneNumber;

    private String area;

    private boolean isMain;

    private FarmType farmType;

    private LocalDate startDate;

    private CropType cropType;

    private LocalDate startDate;

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
