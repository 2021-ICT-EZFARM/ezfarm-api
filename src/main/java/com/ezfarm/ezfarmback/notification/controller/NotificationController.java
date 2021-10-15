package com.ezfarm.ezfarmback.notification.controller;

import com.ezfarm.ezfarmback.notification.domain.Notification;
import com.ezfarm.ezfarmback.notification.domain.NotificationSupport;
import com.ezfarm.ezfarmback.notification.dto.NotificationResponse;
import com.ezfarm.ezfarmback.notification.service.NotificationService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  @ApiOperation(value = "이전 푸시 알림 조회")
  @GetMapping("/api/notification")
  public ResponseEntity<List<NotificationResponse>> findNotifications(@CurrentUser User user,
      @RequestParam Long farmId) {
    return ResponseEntity.ok(notificationService.findNotifications(user, farmId));
  }

  @ApiOperation(value = "푸시 알림 보내기(테스트)")
  @PostMapping("/api/notification/test")
  public ResponseEntity<Void> sendNotification(@CurrentUser User user) {
    Notification notification = Notification.builder()
        .user(user)
        .content("경고 알림 테스트 입니다.")
        .build();
    notificationService.saveNotifications(notification);
    notificationSupport.sendNotification(notification);
    return ResponseEntity.ok().build();
  }
}
