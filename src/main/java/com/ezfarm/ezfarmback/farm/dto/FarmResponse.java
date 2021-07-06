package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmResponse {

<<<<<<< Updated upstream
  private String address;
=======
    private Long id;

    private String address;
>>>>>>> Stashed changes

  private String phoneNumber;

  private String area;

  private boolean isMain;

  private FarmType farmType;

  private CropType cropType;

<<<<<<< Updated upstream
  private LocalDate startDate;
=======
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
>>>>>>> Stashed changes

}
