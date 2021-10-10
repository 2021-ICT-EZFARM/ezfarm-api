package com.ezfarm.ezfarmback.iot.domain;

public enum OnOff {

  ON, OFF;

  public boolean isNotEquals(String onOff) {
    return !this.toString().equals(onOff);
  }
}
