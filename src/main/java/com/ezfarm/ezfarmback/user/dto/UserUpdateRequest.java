package com.ezfarm.ezfarmback.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank
    private String name;
    private String phoneNumber;
    private String address;
    private String imageUrl;
}
