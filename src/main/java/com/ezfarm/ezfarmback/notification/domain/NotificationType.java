package com.ezfarm.ezfarmback.notification.domain;

import lombok.Getter;

@Getter
public enum NotificationType {
  REMOTE("제어"),
  WARN("경고");

  private final String name;

  NotificationType(String name) {
    this.name = name;
  }
}
