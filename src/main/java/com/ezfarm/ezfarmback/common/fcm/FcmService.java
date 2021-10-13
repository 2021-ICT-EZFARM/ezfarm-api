package com.ezfarm.ezfarmback.common.fcm;

import com.ezfarm.ezfarmback.alert.dto.AlertRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmService {

  public final Map<Long, String> tokenMap = new ConcurrentHashMap<>();

  public void send(AlertRequest alertRequest) {
    try {
      Message message = Message.builder().setToken(alertRequest.getToken()).setWebpushConfig(
              WebpushConfig.builder().putHeader("key", "300")
                  .setNotification(new WebpushNotification(null, alertRequest.getMessage())).build())
          .build();
      FirebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      log.error(e.getMessage());
    }
  }

  public void register(Long userId, String token) {
    tokenMap.put(userId, token);
  }

  public void deleteToken(Long userId) {
    tokenMap.remove(userId);
  }

  public String getToken(Long userId) {
    return tokenMap.get(userId);
  }

  public boolean validateFcmToken(Long userId) {
    return tokenMap.containsKey(userId);
  }
}
