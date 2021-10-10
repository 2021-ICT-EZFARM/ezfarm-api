package com.ezfarm.ezfarmback.iot.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.iot.domain.OnOff;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import com.ezfarm.ezfarmback.iot.dto.RemoteResponse;
import com.ezfarm.ezfarmback.iot.dto.ScreenResponse;
import com.ezfarm.ezfarmback.iot.service.IotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@DisplayName("IOT 단위 테스트(Controller)")
@WebMvcTest(controllers = IotController.class)
public class IotControllerTest extends CommonApiTest {

  @MockBean
  IotService iotService;

  RemoteRequest remoteRequest;

  RemoteResponse remoteResponse;

  ScreenResponse screenResponse;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    remoteResponse = RemoteResponse.builder()
        .id(1L)
        .water(OnOff.ON)
        .temperature("37.5")
        .illuminance(OnOff.OFF)
        .co2(OnOff.OFF)
        .build();
    remoteRequest = RemoteRequest.builder()
        .remoteId(1L)
        .water(OnOff.ON.toString())
        .temperature("37.5")
        .illuminance(OnOff.OFF.toString())
        .co2(OnOff.OFF.toString())
        .build();
    screenResponse = ScreenResponse.builder()
        .imageUrl("image")
        .cropCondition("22.4")
        .measureTime("9")
        .build();
  }


  @WithMockCustomUser
  @DisplayName("농가 제어값 조회")
  @Test
  void findRemote() throws Exception {
    when(iotService.findRemote(any())).thenReturn(remoteResponse);

    mockMvc.perform(get(String.format("/api/iot/remote?farmId=%d", 1L)))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(remoteResponse)))
        .andDo(print());
  }

  @WithMockCustomUser
  @DisplayName("농가 제어값 수정")
  @Test
  void updateRemote() throws Exception {
    doNothing().when(iotService).updateRemote(any(), any());

    mockMvc.perform(patch("/api/iot/remote")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(remoteRequest)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @WithMockCustomUser
  @DisplayName("실시간 화면을 조회한다.")
  @Test
  void findLiveScreen() throws Exception {
    when(iotService.findLiveScreen(any(), any())).thenReturn(screenResponse);

    mockMvc.perform(get(String.format("/api/iot/live-screen?farmId=%d", 1L)))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(screenResponse)))
        .andDo(print());
  }
}
