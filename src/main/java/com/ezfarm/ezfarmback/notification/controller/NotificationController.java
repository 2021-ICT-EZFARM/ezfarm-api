package com.ezfarm.ezfarmback.notification.controller;

import com.ezfarm.ezfarmback.notification.domain.Notification;
import com.ezfarm.ezfarmback.notification.domain.NotificationSupport;
import com.ezfarm.ezfarmback.notification.domain.NotificationType;
import com.ezfarm.ezfarmback.notification.dto.NotificationResponse;
import com.ezfarm.ezfarmback.notification.service.NotificationService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "푸시 알림 API")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  private final NotificationSupport notificationSupport;

  @GetMapping("/api/notification")
  public ResponseEntity<List<NotificationResponse>> findNotifications(@CurrentUser User user,
      @RequestParam Long farmId) {
    return ResponseEntity.ok(notificationService.findNotifications(user, farmId));
  }

  @PostMapping("/api/notification/test")
  public ResponseEntity<Void> findNotifications(@CurrentUser User user,
      @RequestBody String content) {
    Notification notification = Notification.builder()
        .user(user)
        .type(NotificationType.WARN)
        .content(content)
        .build();
    notificationSupport.sendNotification(notification);
    return ResponseEntity.ok().build();
  }
}
