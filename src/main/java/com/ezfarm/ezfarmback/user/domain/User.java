package com.ezfarm.ezfarmback.user.domain;

import com.ezfarm.ezfarmback.common.domain.BaseTimeEntity;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateUser(UserUpdateRequest userUpdateRequest, String storeFileName) {
        this.phoneNumber = userUpdateRequest.getPhoneNumber();
        this.address = userUpdateRequest.getAddress();
        this.imageUrl = storeFileName;
    }

}
