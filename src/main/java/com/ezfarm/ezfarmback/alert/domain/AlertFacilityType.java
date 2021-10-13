package com.ezfarm.ezfarmback.alert.domain;

import lombok.Getter;

@Getter
public enum AlertFacilityType {
  TMP("온도"),
  HUMIDITY("습도"),
  IMN("조도"),
  C02("이산화탄소"),
  PH("급액"),
  MOS("토양 수분");

  private final String name;

  AlertFacilityType(String name) {
    this.name = name;
  }
}
