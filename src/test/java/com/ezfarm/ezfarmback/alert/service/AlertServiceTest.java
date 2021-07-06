package com.ezfarm.ezfarmback.alert.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.alert.domain.AlertRange;
import com.ezfarm.ezfarmback.alert.domain.AlertRangeRepository;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("알림/알림 범위 단위 테스트(Service)")
public class AlertServiceTest {

    @Mock
    private AlertRangeRepository alertRangeRepository;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private ModelMapper modelMapper;

    private AlertService alertService;

    Farm farm;

    AlertRange alertRange;

    @BeforeEach
    void setUp() {
        alertService = new AlertService(alertRangeRepository, farmRepository, modelMapper);

        farm = Farm.builder()
            .name("테스트 농가")
            .build();

        alertRange = new AlertRange(farm);
    }

    @DisplayName("알림 범위를 조회한다.")
    @Test
    void findAlertRange_success() {
        AlertRangeResponse alertRangeResponse = new AlertRangeResponse();

        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        when(alertRangeRepository.findByFarm(any())).thenReturn(ofNullable(alertRange));
        when(modelMapper.map(any(), any())).thenReturn(alertRangeResponse);

        AlertRangeResponse result = alertService.findAlertRange(1L);

        Assertions.assertAll(
            () -> assertThat(result.getTmpMax()).isEqualTo(alertRange.getTmpMax()),
            () -> assertThat(result.getTmpMin()).isEqualTo(alertRange.getTmpMin()),
            () -> assertThat(result.getCo2Max()).isEqualTo(alertRange.getCo2Max())
        );
    }

    @DisplayName("알림 범위가 존재하지 않으면 새로 생성한다.")
    @Test
    void findAlertRange_notFound_alertRange_failure() {
        AlertRangeResponse alertRangeResponse = new AlertRangeResponse();
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        when(alertRangeRepository.findByFarm(any())).thenReturn(Optional.empty());
        when(modelMapper.map(any(), any())).thenReturn(alertRangeResponse);

        AlertRangeResponse result = alertService.findAlertRange(1L);

        Assertions.assertAll(
            () -> assertThat(result.getTmpMax()).isEqualTo(alertRange.getTmpMax()),
            () -> assertThat(result.getTmpMin()).isEqualTo(alertRange.getTmpMin()),
            () -> assertThat(result.getCo2Max()).isEqualTo(alertRange.getCo2Max())
        );
    }

    @DisplayName("알림 범위 조회 시 농가이 존재하지 않으면 에러가 발생한다.")
    @Test
    void findAlertRange_invalid_farm_failure() {
        when(farmRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> alertService.findAlertRange(1L))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INVALID_FARM_ID.getMessage());
    }

    @DisplayName("알림 범위를 수정한다.")
    @Test
    void updateAlertRange_success() {
        AlertRangeRequest alertRangeRequest = new AlertRangeRequest();
        alertRangeRequest.setTmpMax((float) 10.1);
        alertRangeRequest.setTmpMin((float) 5.7);

        when(alertRangeRepository.findById(any())).thenReturn(ofNullable(alertRange));
        alertService.updateAlertRange(1L, alertRangeRequest);

        Assertions.assertAll(
            () -> assertThat(alertRange.getTmpMax()).isEqualTo(alertRangeRequest.getTmpMax()),
            () -> assertThat(alertRange.getTmpMin()).isEqualTo(alertRangeRequest.getTmpMin()),
            () -> assertThat(alertRange.getCo2Max()).isEqualTo(alertRangeRequest.getCo2Max())
        );
    }

    @DisplayName("알림 범위 수정 시 엔티티가 없으면 에러를 발생한다.")
    @Test
    void updateAlertRange_notFound_alertRange_failure() {
        AlertRangeRequest alertRangeRequest = new AlertRangeRequest();
        when(alertRangeRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> alertService.updateAlertRange(1L, alertRangeRequest))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
