package com.ezfarm.ezfarmback.alert.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.alert.service.AlertService;
import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@DisplayName("알림/알림 범위 단위 테스트(Controller)")
@WebMvcTest(controllers = AlertController.class)
public class AlertControllerTest extends CommonApiTest {

    @MockBean
    AlertService alertService;

    @DisplayName("알림 범위를 조회한다.")
    @WithMockCustomUser
    @Test
    void findAlertRange() throws Exception {
        AlertRangeResponse alertRangeResponse = new AlertRangeResponse();
        when(alertService.findAlertRange(any())).thenReturn(alertRangeResponse);

        mockMvc.perform(get(String.format("/api/alert/alert-range?farmId=%d", 1L)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("알림 범위를 수정한다.")
    @WithMockCustomUser
    @Test
    void updateAlertRange() throws Exception {

        AlertRangeRequest alertRangeRequest = new AlertRangeRequest();
        alertRangeRequest.setTmpMax((float) 10.1);
        alertRangeRequest.setTmpMin((float) 5.7);

        doNothing().when(alertService).updateAlertRange(any(), any());

        mockMvc.perform(patch(String.format("/api/alert/alert-range?alertRangeId=%d", 1L))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(alertRangeRequest)))
            .andExpect(status().isOk())
            .andDo(print());

    }
}
