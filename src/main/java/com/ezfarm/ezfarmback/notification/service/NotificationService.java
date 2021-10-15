package com.ezfarm.ezfarmback.notification.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
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

  private final FarmRepository farmRepository;

  private final NotificationRepository notificationRepository;

  @Transactional(readOnly = true)
  public List<NotificationResponse> findNotifications(User user, Long farmId) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    farm.validateIsMyFarm(user);
    return NotificationResponse.listOf(
        notificationRepository.findAllByFarmOrderByCreatedDateDesc(farm)
    );
  }

  public Farm validateFarmIdAndGetFarm(Long farmId) {
    return farmRepository.findById(farmId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));
  }

  public void saveNotifications(Notification notification) {
    notificationRepository.save(notification);
  }
}
