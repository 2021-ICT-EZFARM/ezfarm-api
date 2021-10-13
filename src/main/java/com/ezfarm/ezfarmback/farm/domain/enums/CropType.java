package com.ezfarm.ezfarmback.farm.domain.enums;

import lombok.Getter;

@Getter
public enum CropType {

  TOMATO("토마토"),
  STRAWBERRY("딸기"),
  PAPRIKA("파프리카");

  private final String name;

  CropType(String name) {
    this.name = name;
  }
}
