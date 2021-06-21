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

  private String address;

  private String phoneNumber;

  private String area;

  private boolean isMain;

  private FarmType farmType;

  private CropType cropType;

  private LocalDate startDate;

}
