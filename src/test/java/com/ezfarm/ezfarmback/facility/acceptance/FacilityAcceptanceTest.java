package com.ezfarm.ezfarmback.facility.acceptance;

import static java.util.List.of;

import com.ezfarm.ezfarmback.common.db.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.facility.acceptance.step.FacilityAcceptanceStep;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.domain.hour.FacilityRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("센서값 통합 테스트")
public class FacilityAcceptanceTest extends CommonAcceptanceTest {

  AuthResponse authResponse;

  FarmRequest farmRequest;

  Facility facility;

  @Autowired
  FacilityRepository facilityRepository;

  @Autowired
  FarmRepository farmRepository;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
    authResponse = getAuthResponse(loginRequest);

    farmRequest = new FarmRequest(
        "경기",
        "테스트 이름",
        "010-2222-2222",
        "100",
        true,
        FarmType.GLASS,
        CropType.PAPRIKA,
        LocalDate.now()
    );

  }

  @DisplayName("메인 농가의 센서값을 조회한다.(메인페이지)")
  @Test
  void findMainFarmFacility() throws Exception {
    ExtractableResponse<Response> farmResponse = FarmAcceptanceStep
        .requestToCreateFarm(authResponse, farmRequest, objectMapper);
    Long farmId = FarmAcceptanceStep.getLocation(farmResponse);
    createFacilityData(farmId);

    ExtractableResponse<Response> response = FacilityAcceptanceStep.requestToFindMainFarmFacility(
        authResponse);
    FacilityResponse facilityResponse = response.jsonPath().getObject(".", FacilityResponse.class);
    FacilityAcceptanceStep.assertThatFindMainFarmFacility(facilityResponse, facility);
  }

  void createFacilityData(Long farmId) {
    Farm farm = farmRepository.findById(farmId).get();
    facility = Facility.builder()
        .farm(farm)
        .tmp(21.4f)
        .humidity(12.5f)
        .illuminance(11f)
        .co2(16.2f)
        .ph(18.9f)
        .mos(55.1f)
        .measureDate(LocalDateTime.of(2021, 8, 20, 19, 19))
        .build();

    Facility oldFacility = Facility.builder()
        .farm(farm)
        .tmp(11.4f)
        .humidity(18.5f)
        .illuminance(72.2f)
        .co2(26.2f)
        .ph(30f)
        .mos(55f)
        .measureDate(LocalDateTime.of(2020, 8, 20, 19, 19))
        .build();

    facilityRepository.saveAll(of(facility, oldFacility));
  }
}
