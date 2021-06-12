package com.ezfarm.ezfarmback.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String phoneNumber;
    private String address;
    private String imageUrl;
}
