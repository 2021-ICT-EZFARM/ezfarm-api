package com.ezfarm.ezfarmback.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

  private Long id;
  private String email;
  private String name;
  private String phoneNumber;
  private String address;
  private String imageUrl;

  @Builder
  public UserResponse(Long id, String email, String name, String phoneNumber,
      String address, String imageUrl) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.imageUrl = imageUrl;
  }
}
