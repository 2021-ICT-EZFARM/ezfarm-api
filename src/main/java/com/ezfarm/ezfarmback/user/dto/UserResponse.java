package com.ezfarm.ezfarmback.user.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String imageUrl;

}
