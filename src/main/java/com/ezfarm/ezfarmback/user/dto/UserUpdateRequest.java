package com.ezfarm.ezfarmback.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserUpdateRequest {

    private String phoneNumber;
    private String address;
    private MultipartFile image;
    private IsDefaultImage isDefaultImage;

    @Builder
    public UserUpdateRequest(String phoneNumber, String address,
        MultipartFile image, IsDefaultImage isDefaultImage) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.image = image;
        this.isDefaultImage = isDefaultImage;
    }

    public enum IsDefaultImage {
        Y, N
    }
}
