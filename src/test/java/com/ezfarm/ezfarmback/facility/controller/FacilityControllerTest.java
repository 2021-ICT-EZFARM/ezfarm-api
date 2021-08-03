package com.ezfarm.ezfarmback.facility.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.service.FacilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("시설 단위 테스트(Controller)")
@WebMvcTest(controllers = FacilityController.class)
public class FacilityControllerTest extends CommonApiTest {

    @MockBean
    FacilityService facilityService;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @WithMockCustomUser
    @DisplayName("검색 가능한 기간을 조회한다.")
    @Test
    void createFarm() throws Exception {
        FacilityPeriodResponse facilityPeriodResponse = new FacilityPeriodResponse("2020-1",
            "2020-10");

        when(facilityService.findFacilitySearchPeriod(any())).thenReturn(facilityPeriodResponse);

        mockMvc.perform(get("/api/facility/search-condition/{farmId}", 1L))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
