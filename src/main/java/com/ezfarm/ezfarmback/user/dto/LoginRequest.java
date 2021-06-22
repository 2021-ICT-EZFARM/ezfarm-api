package com.ezfarm.ezfarmback.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @ApiModelProperty(value = "유저 이메일", required = true)
    @NotBlank
    @Email
    private String email;

    @ApiModelProperty(value = "유저 비밀번호", required = true)
    @NotBlank
    private String password;
}
