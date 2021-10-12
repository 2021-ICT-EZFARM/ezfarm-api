package com.ezfarm.ezfarmback.common.fcm;

import com.ezfarm.ezfarmback.alert.domain.AlertFacilityType;
import com.ezfarm.ezfarmback.alert.domain.AlertType;
import com.ezfarm.ezfarmback.alert.dto.AlertRequest;
import com.ezfarm.ezfarmback.alert.service.AlertService;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Slf4j
@Component
@RequiredArgsConstructor
public class FcmListener {

  private final AlertService alertService;

  @EventListener
  public void sendMessage(FcmEvent fcmEvent) {
    Facility facility = fcmEvent.getFacility();
    AlertFacilityType alertFacilityType = fcmEvent.getAlertFacilityType();
    AlertType alertType = fcmEvent.getAlertType();
    User user = facility.getFarm().getUser();

    AlertRequest alertRequest = AlertRequest.builder()
        .title("실시간 농가 예외")
        .token(alertService.getToken(user.getId()))
        .message(alertFacilityType + "이 " + alertType + "되었습니다.")
        .build();
    alertService.sendNotification(alertRequest);
  }
}
