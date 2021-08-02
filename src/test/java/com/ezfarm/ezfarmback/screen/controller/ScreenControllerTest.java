package com.ezfarm.ezfarmback.screen.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.screen.dto.ScreenResponse;
import com.ezfarm.ezfarmback.screen.service.ScreenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("제어 단위 테스트(Controller)")
@WebMvcTest(controllers = ScreenController.class)
public class ScreenControllerTest extends CommonApiTest {

  @MockBean
  ScreenService screenService;

  ScreenResponse screenResponse;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    screenResponse = new ScreenResponse("test-url", 22.4F, "2021-09-12");
  }

  @WithMockCustomUser
  @DisplayName("실시간 화면을 조회한다.")
  @Test
  void findScreen() throws Exception {
    when(screenService.findScreen(any(), any())).thenReturn(screenResponse);

    mockMvc.perform(get(String.format("/api/screen?farmId=%d", 1L)))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(screenResponse)))
        .andDo(print());
  }

}
