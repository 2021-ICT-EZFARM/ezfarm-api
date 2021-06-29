package com.ezfarm.ezfarmback.farm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@DisplayName("농장 단위 테스트(Controller)")
@WebMvcTest(controllers = FarmController.class)
class FarmControllerTest extends CommonApiTest {

    @MockBean
    FarmService farmService;

    @MockBean
    FarmRepository farmRepository;

    FarmRequest farmRequest;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        farmRequest = new FarmRequest(
            "서울",
            "010-1111-1111",
            "100",
            false,
            FarmType.GLASS,
            CropType.PAPRIKA,
            LocalDate.now()
        );
    }

    @WithMockCustomUser
    @DisplayName("농장 생성")
    @Test
    void createFarm() throws Exception {

        when(farmService.createFarm(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/api/farm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(farmRequest)))
            .andExpect(status().isCreated())
            .andDo(print());
    }
}