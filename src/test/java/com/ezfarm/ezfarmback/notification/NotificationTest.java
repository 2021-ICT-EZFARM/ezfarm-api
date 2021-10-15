package com.ezfarm.ezfarmback.notification;

import static com.ezfarm.ezfarmback.config.StompWebSocketConfig.WS_ENDPOINT;
import static com.ezfarm.ezfarmback.notification.domain.NotificationSupport.SUBSCRIBE;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.notification.domain.Notification;
import com.ezfarm.ezfarmback.notification.domain.NotificationSupport;
import com.ezfarm.ezfarmback.notification.dto.NotificationResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@DisplayName("알림 테스트(STOMP + Web Socket)")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationTest {

  @LocalServerPort
  int port;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  NotificationSupport notificationSupport;

  private StompSession session;

  private BlockingQueue<String> blockingQueue;

  @BeforeEach
  public void setUp() throws Exception {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    WebSocketStompClient stompClient = new WebSocketStompClient(
        new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));

    // STOMP + Web Socket 연결
    session = stompClient.connect(format("ws://localhost:%d" + WS_ENDPOINT, port),
        new StompSessionHandlerAdapter() {
        }).get(60, SECONDS);

    stompClient.setMessageConverter(new ByteArrayMessageConverter());
  }

  @Disabled
  @DisplayName("푸시 알림을 보낸다.")
  @Test
  void verifyNotificationIsReceived() throws JsonProcessingException, InterruptedException {
    long userId = 1L;
    Notification notification = Notification.builder()
        .user(User.builder().id(userId).build())
        .farm(Farm.builder().name("테스트 농가").build())
        .content("알림 테스트 입니다.")
        .build();
    blockingQueue = new LinkedBlockingDeque<>();

    session.subscribe(SUBSCRIBE + userId, new defaultSessionHandlerAdapter());
    notificationSupport.sendNotification(notification);
    NotificationResponse response = objectMapper.readValue(blockingQueue.poll(1, SECONDS),
        NotificationResponse.class);

    assertThat(response.getContent()).isEqualTo(notification.getContent());
  }


  public class defaultSessionHandlerAdapter extends StompSessionHandlerAdapter {

    @Override
    public Type getPayloadType(StompHeaders headers) {
      return byte[].class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
      blockingQueue.add(new String((byte[]) payload));
    }
  }
}
