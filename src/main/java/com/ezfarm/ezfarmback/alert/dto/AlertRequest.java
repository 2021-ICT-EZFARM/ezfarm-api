package com.ezfarm.ezfarmback.alert.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertRequest {

  private String title;
  private String message;
  private String token;

  @Builder
  public AlertRequest(String title, String message, String token) {
    this.title = title;
    this.message = message;
    this.token = token;
  }

}
