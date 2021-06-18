package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class FarmForm {

    private String address;

    private String phoneNumber;

    private String area;

    private Boolean isMain;

    private FarmType farmType;

    private CropType cropType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    private User user;
}
