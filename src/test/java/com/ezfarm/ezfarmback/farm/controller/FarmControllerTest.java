package com.ezfarm.ezfarmback.farm.controller;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

@DisplayName("농가 단위 테스트(Controller)")
@WebMvcTest(controllers = FarmController.class)
class FarmControllerTest extends CommonApiTest {

    @MockBean
    FarmService farmService;

    @MockBean
    FarmRepository farmRepository;

    FarmRequest farmRequest;

    FarmResponse farmResponse;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        farmRequest = new FarmRequest(
            "서울", "테스트 이름", "010-1234-1234",
            "100", false, FarmType.GLASS,
            CropType.PAPRIKA, LocalDate.now()
        );
        farmResponse = new FarmResponse();
    }

    @WithMockCustomUser
    @DisplayName("농가를 생성한다.")
    @Test
    void createFarm() throws Exception {
        when(farmService.createFarm(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/api/farm/me")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(farmRequest)))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("나의 모든 농가를 조회한다.")
    @Test
    void findMyFarms() throws Exception {
        when(farmService.findMyFarms(any())).thenReturn(singletonList(farmResponse));

        mockMvc.perform(get("/api/farm/me")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("나의 농가를 조회한다.")
    @Test
    void findMyFarm() throws Exception {
        when(farmService.findMyFarm(any())).thenReturn(farmResponse);

        mockMvc.perform(get("/api/farm/me/{farmId}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("나의 농가를 수정한다.")
    @Test
    void updateFarm() throws Exception {
        doNothing().when(farmService).updateMyFarm(any(), any(), any());

        mockMvc.perform(patch("/api/farm/me/{farmId}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(farmRequest)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("나의 농가를 삭제한다.")
    @Test
    void deleteMyFarm() throws Exception {
        doNothing().when(farmService).deleteMyFarm(any(), any());

        mockMvc.perform(delete("/api/farm/me/{farmId}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("타 농가를 조회한다.")
    @Test
    void findOtherFarms() throws Exception {
        FarmSearchCond farmSearchCond = new FarmSearchCond(FarmType.GLASS, CropType.PAPRIKA);
        when(farmService.findOtherFarms(any(), any(), any())).thenReturn(Page.empty());

        mockMvc.perform(post("/api/farm/other")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(farmSearchCond)))
            .andExpect(status().isOk())
            .andDo(print());
    }
}