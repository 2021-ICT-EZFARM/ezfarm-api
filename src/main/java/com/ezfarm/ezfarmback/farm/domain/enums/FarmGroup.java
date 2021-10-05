package com.ezfarm.ezfarmback.farm.domain.enums;

import lombok.Getter;

@Getter
public enum FarmGroup {

  BEST("우수 농가"),
  NORMAL("일반 농가");

  private final String name;

  FarmGroup(String name) {
    this.name = name;
  }
}
