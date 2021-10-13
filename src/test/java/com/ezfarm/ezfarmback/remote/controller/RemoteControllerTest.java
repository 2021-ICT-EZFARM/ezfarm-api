package com.ezfarm.ezfarmback.remote.controller;

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
import com.ezfarm.ezfarmback.remote.domain.OnOff;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.ezfarm.ezfarmback.remote.service.RemoteService;
import java.time.LocalDateTime;
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

    RemoteRequest remoteRequest;

    RemoteResponse remoteResponse;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        remoteResponse = new RemoteResponse(1L, OnOff.ON, 37.5f, OnOff.OFF, OnOff.ON,
            LocalDateTime.now(), LocalDateTime.now());
        remoteRequest = new RemoteRequest(1L, OnOff.ON, 37.5f, OnOff.OFF, OnOff.ON);
    }

    @WithMockCustomUser
    @DisplayName("농가 제어값 조회")
    @Test
    void findRemote() throws Exception {
        when(remoteService.findRemote(any(), any())).thenReturn(remoteResponse);

        mockMvc.perform(get(String.format("/api/remote?farmId=%d", 1L)))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(remoteResponse)))
            .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("농가 제어값 수정")
    @Test
    void updateRemote() throws Exception {
        doNothing().when(remoteService).updateRemote(any(), any());

        mockMvc.perform(patch("/api/remote")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(remoteRequest)))
            .andExpect(status().isOk())
            .andDo(print());
    }
}