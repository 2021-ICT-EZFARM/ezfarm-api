package com.ezfarm.ezfarmback.farm.domain.enums;

import lombok.Getter;

@Getter
public enum FarmType {

  VINYL("비닐"),
  GLASS("유리");

  private final String name;

  FarmType(String name) {
    this.name = name;
  }
}
