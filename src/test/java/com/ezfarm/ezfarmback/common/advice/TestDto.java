package com.ezfarm.ezfarmback.common.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    @NotBlank
    private String val1;

    private int val2;

}
