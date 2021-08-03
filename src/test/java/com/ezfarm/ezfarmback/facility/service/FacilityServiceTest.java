package com.ezfarm.ezfarmback.facility.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import java.time.LocalDate;
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
    private FacilityMonthAvgRepository facilityMonthAvgRepository;

    FacilityService facilityService;

    Farm farm;

    @BeforeEach
    void setUp() {
        facilityService = new FacilityService(farmRepository, facilityMonthAvgRepository);

        farm = Farm.builder()
            .id(1L)
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
        when(facilityMonthAvgRepository.findMinAndMinMeasureDateByFarm(farm)).thenReturn(
            periodResponse);

        FacilityPeriodResponse response = facilityService.findFacilitySearchPeriod(1L);

        verify(farmRepository).findById(any());
        verify(facilityMonthAvgRepository).findMinAndMinMeasureDateByFarm(any());

        Assertions.assertAll(
            () -> assertThat(response.getStartDate()).isEqualTo(periodResponse.getStartDate()),
            () -> assertThat(response.getEndDate()).isEqualTo(periodResponse.getEndDate())
        );
    }
}
