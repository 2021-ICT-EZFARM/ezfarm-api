package com.ezfarm.ezfarmback.user.dto;

import com.ezfarm.ezfarmback.user.domain.Role;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String imageUrl;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
