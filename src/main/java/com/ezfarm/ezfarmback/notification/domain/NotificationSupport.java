package com.ezfarm.ezfarmback.notification.domain;

import static com.ezfarm.ezfarmback.config.StompWebSocketConfig.SUB_PREFIX;

import com.ezfarm.ezfarmback.notification.dto.NotificationResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSupport {

  public static final String SUBSCRIBE = SUB_PREFIX + "/notification/";

  private final SimpMessagingTemplate simpMessagingTemplate;

  public void sendNotification(Notification notification) {
    User receiver = notification.getUser();
    NotificationResponse response = NotificationResponse.of(notification);
    simpMessagingTemplate.convertAndSend(SUBSCRIBE + receiver.getId(), response);
  }
}
