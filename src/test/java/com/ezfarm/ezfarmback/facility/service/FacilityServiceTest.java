package com.ezfarm.ezfarmback.facility.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.common.utils.iot.IotUtils;
import com.ezfarm.ezfarmback.facility.domain.FacilityAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.domain.hour.FacilityRepository;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("시설 단위 테스트(Service)")
public class FacilityServiceTest {

  @Mock
  private FarmRepository farmRepository;

  @Mock
  private FacilityDayAvgRepository facilityDayAvgRepository;

  @Mock
  private FacilityMonthAvgRepository facilityMonthAvgRepository;

  @Mock
  private FacilityWeekAvgRepository facilityWeekAvgRepository;

  @Mock
  private IotUtils iotUtils;

  @Mock
  private FacilityRepository facilityRepository;

  FacilityService facilityService;

  Farm farm;

  User user;

  @BeforeEach
  void setUp() {
    facilityService = new FacilityService(farmRepository, facilityDayAvgRepository,
        facilityMonthAvgRepository, facilityWeekAvgRepository, iotUtils, facilityRepository);

    user = User.builder()
        .id(1L)
        .email("test@email.com")
        .name("테스트 유저")
        .password("1234")
        .role(Role.ROLE_USER)
        .build();

    farm = Farm.builder()
        .id(1L)
        .user(user)
        .name("테스트 농가 이름1")
        .address("서울")
        .isMain(false)
        .startDate(LocalDate.now())
        .build();

  }

  @DisplayName("검색 가능한 기간을 검색한다.")
  @Test
  void findFacilitySearchPeriod_success() {
    FacilityPeriodResponse periodResponse = new FacilityPeriodResponse("2020-1", "2021-10");
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(facilityDayAvgRepository.findMinAndMaxMeasureDateByFarm(farm)).thenReturn(
        periodResponse);

    FacilityPeriodResponse response = facilityService.findFacilitySearchPeriod(1L);

    verify(farmRepository).findById(any());
    verify(facilityDayAvgRepository).findMinAndMaxMeasureDateByFarm(any());

    Assertions.assertAll(
        () -> assertThat(response.getStartDate()).isEqualTo(periodResponse.getStartDate()),
        () -> assertThat(response.getEndDate()).isEqualTo(periodResponse.getEndDate())
    );
  }

  @DisplayName("타 농가 일 평균 데이터를 조회한다.")
  @Test
  void findFacilityDailyAvg_success() {
    FacilityDailyAvgRequest facilityDailyAvgRequest = new FacilityDailyAvgRequest("2020", "01");
    FacilityDayAvg facilityDayAvg = FacilityDayAvg.builder()
        .facilityAvg(new FacilityAvg())
        .measureDate(LocalDateTime.of(2020, 1, 1, 0, 0))
        .build();

    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(facilityDayAvgRepository.findAllByFarmAndMeasureDateStartsWith(any(),
        any())).thenReturn(List.of(facilityDayAvg));

    List<FacilityAvgResponse> response = facilityService.findFacilityDailyAvg(1L,
        facilityDailyAvgRequest);

    verify(farmRepository).findById(any());
    verify(facilityDayAvgRepository).findAllByFarmAndMeasureDateStartsWith(any(), any());
    Assertions.assertAll(
        () -> assertThat(response.size()).isEqualTo(1),
        () -> assertThat(response).extracting("measureDate")
            .contains("2020-01-01"),
        () -> assertThat(response).extracting("avgCo2")
            .contains(0.0f)
    );
  }

  @DisplayName("타 농가 주 평균 데이터를 조회한다.")
  @Test
  void findFacilityWeekAvg_success() {
    FacilityWeekAvgRequest facilityWeekAvgRequest = new FacilityWeekAvgRequest("2021-01", null,
        null);
    FacilityWeekAvg facilityWeekAvg = FacilityWeekAvg.builder()
        .facilityAvg(new FacilityAvg())
        .measureDate(LocalDateTime.of(2020, 1, 15, 0, 0))
        .build();

    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(facilityWeekAvgRepository.findAllByFarmAndMeasureDateStartsWith(any(),
        any())).thenReturn(List.of(facilityWeekAvg));

    List<FacilityAvgResponse> response = facilityService.findFacilityWeekAvg(1L,
        facilityWeekAvgRequest);

    verify(farmRepository).findById(any());
    verify(facilityWeekAvgRepository).findAllByFarmAndMeasureDateStartsWith(any(), any());
    Assertions.assertAll(
        () -> assertThat(response.size()).isEqualTo(1),
        () -> assertThat(response).extracting("measureDate")
            .contains("2020-01-3"),
        () -> assertThat(response).extracting("avgCo2")
            .contains(0.0f)
    );
  }

  @DisplayName("타 농가 월 평균 데이터를 조회한다.")
  @Test
  void findFacilityMonthlyAvg_success() {
    FacilityMonthAvgRequest facilityMonthAvgRequest = new FacilityMonthAvgRequest("2020");
    FacilityMonthAvg facilityMonthAvg = FacilityMonthAvg.builder()
        .facilityAvg(new FacilityAvg())
        .measureDate(LocalDateTime.of(2020, 1, 1, 0, 0))
        .build();

    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(facilityMonthAvgRepository.findAllByFarmAndMeasureDateStartsWith(any(),
        any())).thenReturn(List.of(facilityMonthAvg));

    List<FacilityAvgResponse> response = facilityService.findFacilityMonthlyAvg(1L,
        facilityMonthAvgRequest);

    verify(farmRepository).findById(any());
    verify(facilityMonthAvgRepository).findAllByFarmAndMeasureDateStartsWith(any(), any());
    Assertions.assertAll(
        () -> assertThat(response.size()).isEqualTo(1),
        () -> assertThat(response).extracting("measureDate")
            .contains("2020-01"),
        () -> assertThat(response).extracting("avgCo2")
            .contains(0.0f)
    );
  }

  @DisplayName("내 농가의 실시간 센서값을 조회한다.")
  @Test
  void findLiveFacility() {
    //humidity, tmp, illuminance, co2, ph, mos, measureDate;
    String output = "13.7, 26.7, 55.3, 1.2, 22.4, 16.5, 2021-08-12";
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(iotUtils.getLiveSensorValue(any())).thenReturn(output);
    FacilityResponse facilityResponse = facilityService.findLiveFacility(user, 1L);

    Assertions.assertAll(
        () -> assertThat(facilityResponse.getHumidity()).isEqualTo("13.7"),
        () -> assertThat(facilityResponse.getTmp()).isEqualTo("26.7"),
        () -> assertThat(facilityResponse.getIlluminance()).isEqualTo("55.3"),
        () -> assertThat(facilityResponse.getCo2()).isEqualTo("1.2"),
        () -> assertThat(facilityResponse.getPh()).isEqualTo("22.4"),
        () -> assertThat(facilityResponse.getMos()).isEqualTo("16.5"),
        () -> assertThat(facilityResponse.getMeasureDate()).isEqualTo("2021-08-12")
    );
  }

  @DisplayName("메인 농가의 최근 센서값을 조회한다.(메인 페이지)")
  @Test
  void findMainFarmFacility() {
    Farm mainFarm = Farm.builder()
        .id(1L)
        .user(user)
        .name("메인 농가")
        .address("서울")
        .isMain(true)
        .startDate(LocalDate.now())
        .build();

    Facility recentFacility = Facility.builder()
        .farm(mainFarm)
        .tmp(21.4F)
        .humidity(12.5F)
        .illuminance(11)
        .co2(16.2f)
        .ph(18.9f)
        .mos(55.1f)
        .measureDate(LocalDateTime.now())
        .build();

    when(farmRepository.findByUserAndIsMain(any(), anyBoolean())).thenReturn(ofNullable(mainFarm));
    when(facilityRepository.findTop1ByFarmOrderByMeasureDateDesc(any())).thenReturn(
        ofNullable(recentFacility));

    FacilityResponse facilityResponse = facilityService.findMainFarmFacility(user);

    Assertions.assertAll(
        () -> assertThat(facilityResponse.getHumidity()).isEqualTo(
            Float.toString(recentFacility.getHumidity())),
        () -> assertThat(facilityResponse.getTmp()).isEqualTo(
            Float.toString(recentFacility.getTmp())),
        () -> assertThat(facilityResponse.getIlluminance()).isEqualTo(
            Float.toString(recentFacility.getIlluminance())),
        () -> assertThat(facilityResponse.getCo2()).isEqualTo(
            Float.toString(recentFacility.getCo2())),
        () -> assertThat(facilityResponse.getPh()).isEqualTo(
            Float.toString(recentFacility.getPh())),
        () -> assertThat(facilityResponse.getMos()).isEqualTo(
            Float.toString(recentFacility.getMos())),
        () -> assertThat(facilityResponse.getMeasureDate()).isEqualTo(
            recentFacility.getMeasureDate().toString())
    );
  }
}
