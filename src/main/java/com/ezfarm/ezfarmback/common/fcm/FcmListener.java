package com.ezfarm.ezfarmback.common.fcm;

import com.ezfarm.ezfarmback.alert.dto.AlertRequest;
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

  private final FcmService fcmService;

  @EventListener
  public void sendMessage(FcmEvent e) {
    AlertRequest alertRequest = AlertRequest.builder()
        .token(fcmService.getToken(e.getFacility().getFarm().getUser().getId()))
        .message(
            e.getAlertFacilityType().getName() + "가(이) " + e.getAlertType().getName() + "되었습니다.")
        .build();
    fcmService.send(alertRequest);
  }
}
