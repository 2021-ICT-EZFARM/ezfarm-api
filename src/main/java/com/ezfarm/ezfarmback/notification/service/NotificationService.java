package com.ezfarm.ezfarmback.notification.service;

import com.ezfarm.ezfarmback.notification.domain.Notification;
import com.ezfarm.ezfarmback.notification.domain.NotificationRepository;
import com.ezfarm.ezfarmback.notification.dto.NotificationResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

  private final NotificationRepository notificationRepository;

  @Transactional(readOnly = true)
  public List<NotificationResponse> findNotifications(User user) {
    return NotificationResponse.listOf(
        notificationRepository.findAllByUserOrderByCreatedDateDesc(user)
    );
  }

  public Notification saveNotifications(Notification notification) {
    return notificationRepository.save(notification);
  }
}
