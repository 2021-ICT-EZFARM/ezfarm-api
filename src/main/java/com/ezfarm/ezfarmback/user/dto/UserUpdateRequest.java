package com.ezfarm.ezfarmback.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String phoneNumber;
    private String address;
    //private MultipartFile image;
}
