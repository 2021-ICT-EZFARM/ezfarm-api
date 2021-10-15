package com.ezfarm.ezfarmback.notification.dto;

import com.ezfarm.ezfarmback.notification.domain.Notification;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

  String content;

  public static NotificationResponse of(Notification notification) {
    return new NotificationResponse(notification.getContent());
  }

  public static List<NotificationResponse> listOf(List<Notification> notifications) {
    return notifications.stream().map(NotificationResponse::of).collect(Collectors.toList());
  }
}
