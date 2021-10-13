package com.ezfarm.ezfarmback.alert.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertRequest {

  private String message;
  private String token;

  @Builder
  public AlertRequest(String message, String token) {
    this.message = message;
    this.token = token;
  }
}
