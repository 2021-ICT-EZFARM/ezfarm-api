package com.ezfarm.ezfarmback.facility.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.service.FacilityService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@DisplayName("시설 단위 테스트(Controller)")
@WebMvcTest(controllers = FacilityController.class)
public class FacilityControllerTest extends CommonApiTest {

  @MockBean
  FacilityService facilityService;

  FacilityResponse facilityResponse;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();

    facilityResponse = FacilityResponse.builder()
        .tmp("13.7")
        .humidity("26.7")
        .illuminance("55.3")
        .co2("1.2")
        .ph("22.4")
        .mos("16.5")
        .measureDate("2021-08-12")
        .build();
  }

  @WithMockCustomUser
  @DisplayName("검색 가능한 기간을 조회한다.")
  @Test
  void findFacilitySearchPeriod() throws Exception {
    FacilityPeriodResponse facilityPeriodResponse = new FacilityPeriodResponse("2020-1",
        "2020-10");

    when(facilityService.findFacilitySearchPeriod(any())).thenReturn(facilityPeriodResponse);

    mockMvc.perform(get("/api/facility/search-condition/{farmId}", 1L))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @WithMockCustomUser
  @DisplayName("타 농가 일 평균 데이터를 조회한다.")
  @Test
  void findFacilityDailyAvg() throws Exception {
    FacilityAvgResponse facilityResponse = FacilityAvgResponse.builder()
        .avgCo2(1)
        .measureDate("2020-01-01")
        .build();

    when(facilityService.findFacilityDailyAvg(any(), any())).thenReturn(
        List.of(facilityResponse));

    mockMvc.perform(get("/api/facility/daily-avg/{farmId}?year=%s&month=%s", 1L, "2021", "01"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @WithMockCustomUser
  @DisplayName("타 농가 주 평균 데이터를 조회한다.")
  @Test
  void findFacilityWeekAvg() throws Exception {
    FacilityAvgResponse facilityResponse = FacilityAvgResponse.builder()
        .avgCo2(1)
        .measureDate("2020-01-01")
        .build();

    when(facilityService.findFacilityDailyAvg(any(), any())).thenReturn(
        List.of(facilityResponse));

    mockMvc.perform(
            get("/api/facility/week-avg/{farmId}?dateOne=%s&dateTwo=%s&dateThr=%s", 1L, "2021-01",
                "2021-02", "2021-03"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @WithMockCustomUser
  @DisplayName("타 농가 월 평균 데이터를 조회한다.")
  @Test
  void findFacilityYearAvg() throws Exception {
    FacilityAvgResponse facilityResponse = FacilityAvgResponse.builder()
        .avgCo2(1)
        .measureDate("2020-01-01")
        .build();

    when(facilityService.findFacilityMonthlyAvg(any(), any())).thenReturn(
        List.of(facilityResponse));

    mockMvc.perform(get("/api/facility/monthly-avg/{farmId}?year=%s", 1L, "2021"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @WithMockCustomUser
  @DisplayName("내 농가 실시간 센서값을 조회한다.")
  @Test
  void findLiveFacility() throws Exception {
    when(facilityService.findLiveFacility(any(), any())).thenReturn(facilityResponse);

    mockMvc.perform(get("/api/facility/{farmId}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(facilityResponse)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @WithMockCustomUser
  @DisplayName("메인 농가의 최근 센서값을 조회한다.")
  @Test
  void findMainFarmFacility() throws Exception {
    when(facilityService.findMainFarmFacility(any())).thenReturn(facilityResponse);

    mockMvc.perform(get("/api/facility/recent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(facilityResponse)))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
