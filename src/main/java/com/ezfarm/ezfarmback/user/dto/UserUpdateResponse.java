package com.ezfarm.ezfarmback.user.dto;

import com.ezfarm.ezfarmback.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateResponse {

    private String phoneNumber;
    private String address;
    private String imageUrl;

    public static UserUpdateResponse of(User user) {
        return new UserUpdateResponse(
            user.getPhoneNumber(),
            user.getAddress(),
            user.getImageUrl()
        );
    }
}
