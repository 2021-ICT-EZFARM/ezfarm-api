package com.ezfarm.ezfarmback.alert.domain;

import lombok.Getter;

@Getter
public enum AlertType {
  EXCEED("초과"),
  UNDER("미달");

  private final String name;

  AlertType(String name) {
    this.name = name;
  }
}
