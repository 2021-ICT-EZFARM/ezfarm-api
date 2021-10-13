package com.ezfarm.ezfarmback.common.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmInitializer {

  private static final String firebaseConfig = "ezfarm-back-firebase-adminsdk-i6piu-63998a7f47.json";

  @PostConstruct
  public void initialize() {
    try {
      FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(
          GoogleCredentials.fromStream(new ClassPathResource(firebaseConfig).getInputStream()))
          .build();
      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        log.info("firebase success initialized");
      }
    } catch (Exception e) {
      log.info("firebase error");
    }
  }
}
