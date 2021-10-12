package com.ezfarm.ezfarmback.common.fcm;

import com.ezfarm.ezfarmback.alert.dto.AlertRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmService {

  public void send(AlertRequest alertRequest) throws Exception {
    Message message = Message.builder()
        .setToken(alertRequest.getToken())
        .setWebpushConfig(WebpushConfig.builder().putHeader("key", "300")
        .setNotification(new WebpushNotification(alertRequest.getTitle(), alertRequest.getMessage())).build())
        .build();

    String response = FirebaseMessaging.getInstance().send(message).toString();
    log.info("보낸 message: {}", response);
  }
}
