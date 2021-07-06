package com.ezfarm.ezfarmback.user.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    private String address;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String email, String password, String name, String phoneNumber,
        String address, String imageUrl, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public String roleName() {
        return role.name();
    }

    public void updateUser(UserUpdateRequest userUpdateRequest) {
        this.name = userUpdateRequest.getName();
        this.phoneNumber = userUpdateRequest.getPhoneNumber();
        this.address = userUpdateRequest.getAddress();
        this.imageUrl = userUpdateRequest.getImageUrl();
    }

}
