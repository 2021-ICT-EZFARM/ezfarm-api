package com.ezfarm.ezfarmback.remote.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.remote.domain.RemoteRepository;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.ezfarm.ezfarmback.remote.service.RemoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@DisplayName("제어 단위 테스트(Controller)")
@WebMvcTest(controllers = RemoteController.class)
class RemoteControllerTest extends CommonApiTest {

  @MockBean
  RemoteService remoteService;

  RemoteResponse remoteResponse;

  String jsonString;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();

    jsonString = "{tmp: 30, humidity: 30, illuminance: 30, co2: 30, ph: 30, mos: 30}";

    remoteResponse = new RemoteResponse(jsonString, false);
  }

  @WithMockCustomUser
  @DisplayName("농장 제어값 조회")
  @Test
  void viewRemote() throws Exception {
    when(remoteService.viewRemote(any())).thenReturn(remoteResponse);

    mockMvc.perform(get("/api/farm/1/remote"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().string(objectMapper.writeValueAsString(remoteResponse)))
        .andDo(print());
  }
}