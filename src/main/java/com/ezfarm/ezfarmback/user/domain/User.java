package com.ezfarm.ezfarmback.user.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
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
import org.springframework.util.StringUtils;

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

  public static User create(SignUpRequest request, String encodedPassword) {
    return User.builder()
        .email(request.getEmail())
        .name(request.getName())
        .password(encodedPassword)
        .role(Role.ROLE_USER)
        .build();
  }

  public void update(UserUpdateRequest request) {
    this.phoneNumber = request.getPhoneNumber();
    this.address = request.getAddress();
  }

  public void updateImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void deleteImageUrl() {
    imageUrl = null;
  }

  public boolean hasImage() {
    return StringUtils.hasText(imageUrl);
  }
}
