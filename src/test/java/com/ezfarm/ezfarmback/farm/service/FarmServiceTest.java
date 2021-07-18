package com.ezfarm.ezfarmback.farm.service;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("농가 단위 테스트(Service)")
public class FarmServiceTest {

    @Mock
    FarmRepository farmRepository;

    @Mock
    ModelMapper modelMapper;

    FarmService farmService;

    User user;

    Farm farm;

    FarmRequest farmRequest;

    FarmResponse farmResponse;

    @BeforeEach
    void setUp() {
        farmService = new FarmService(farmRepository, modelMapper);

        user = User.builder()
            .id(1L)
            .name("테스트 이름")
            .email("test@email.com")
            .password("비밀번호")
            .role(Role.ROLE_USER)
            .build();

        farm = Farm.builder()
            .name("테스트 농가 이름1")
            .address("서울")
            .isMain(false)
            .startDate(LocalDate.now())
            .build();

        farmRequest = FarmRequest.builder()
            .name("테스트 농가 이름2")
            .address("경기")
            .build();

        farmResponse = FarmResponse.builder()
            .id(1L)
            .name("테스트 농가 이름1")
            .address("서울")
            .build();
    }

    @DisplayName("농가 시작일이 없는 농가를 생성한다.")
    @Test
    void createFarm_startDate_null_success() {
        when(modelMapper.map(any(), any())).thenReturn(farm);
        when(farmRepository.save(any())).thenReturn(farm);

        farmService.createFarm(user, farmRequest);

        assertThat(farm.getUser()).isEqualTo(user);
        verify(farmRepository).save(any());
    }

    @DisplayName("농가 시작일이 농가 생성일 이후인 농가를 생성한다.")
    @Test
    void createFarm_startDate_notNull_success() {
        when(modelMapper.map(any(), any())).thenReturn(farm);
        when(farmRepository.save(any())).thenReturn(farm);
        farmRequest.setStartDate(LocalDate.now());

        farmService.createFarm(user, farmRequest);

        assertThat(farm.getUser()).isEqualTo(user);
        verify(modelMapper).map(any(), any());
    }

    @DisplayName("농가 시작일이 농가 생성일보다 과거이면 예외가 발생한다.")
    @Test
    void createFarm_failure_customException() {
        farmRequest.setStartDate(LocalDate.of(2000, 1, 1));

        assertThatThrownBy(() -> farmService.createFarm(user, farmRequest))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INVALID_FARM_START_DATE.getMessage());
    }

    @DisplayName("나의 모든 농가를 조회한다.")
    @Test
    void findMyFarms_success() {
        farm.setUser(user);
        when(farmRepository.findAllByUser(any())).thenReturn(singletonList(farm));
        when(modelMapper.map(any(), any())).thenReturn(farmResponse);

        List<FarmResponse> farmResponses = farmService.findMyFarms(user);

        assertThat(farmResponses.size()).isEqualTo(1);
    }

    @DisplayName("나의 농가를 조회한다")
    @Test
    void findFarm_success() {
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        when(modelMapper.map(any(), any())).thenReturn(farmResponse);

        FarmResponse response = farmService.findMyFarm(1L);

        Assertions.assertAll(
            () -> assertThat(response.getAddress()).isEqualTo(farm.getAddress()),
            () -> assertThat(response.getPhoneNumber()).isEqualTo(farm.getPhoneNumber())
        );
    }

    @DisplayName("존재하지 않는 농가일 경우 예외가 발생한다")
    @Test
    void findFarm_is_not_owner_failure() {
        when(farmRepository.findById(any())).thenReturn(empty());

        assertThatThrownBy(() -> farmService.findMyFarm(1L))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INVALID_FARM_ID.getMessage());
    }

    @DisplayName("나의 농가를 수정한다.")
    @Test
    void updateFarm_success() {
        farm.setUser(user);
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));

        farmService.updateMyFarm(user, 1L, farmRequest);

        Assertions.assertAll(
            () -> assertThat(farm.getAddress()).isEqualTo(farmRequest.getAddress()),
            () -> assertThat(farm.getName()).isEqualTo(farmRequest.getName())
        );
    }

    @DisplayName("새로운 메인 농가로 수정한다.")
    @Test
    void updateFarm_success_change_main() {
        farm.setUser(user);
        farmRequest.setMain(true);
        Farm prevMainFarm = Farm.builder()
            .isMain(true)
            .build();

        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        when(farmRepository.findByIsMainAndUser(anyBoolean(), any()))
            .thenReturn(of(prevMainFarm));

        farmService.updateMyFarm(user, 1L, farmRequest);

        Assertions.assertAll(
            () -> assertThat(farm.isMain()).isEqualTo(true),
            () -> assertThat(prevMainFarm.isMain()).isEqualTo(false)
        );
    }

    @DisplayName("농가 재배 시작 일자가 농가 생성 일자보다 빠르면 예외가 발생한다.")
    @Test
    void updateFarm_failure_startDateException() {
        farm.setUser(user);
        farm.setCreatedDate(LocalDateTime.now());
        farmRequest.setStartDate(LocalDate.now().minusDays(1));

        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));

        assertThatThrownBy(() -> farmService.updateMyFarm(user, 1L, farmRequest))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INVALID_FARM_START_DATE.getMessage());
    }

    @DisplayName("나의 농가를 삭제한다.")
    @Test
    void deleteFarm_success() {
        farm.setUser(user);
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        farmService.deleteMyFarm(user, 1L);

        verify(farmRepository).delete(farm);
    }

    @DisplayName("자신의 농가가 아닌 농가를 삭제하면 예외가 발생한다.")
    @Test
    void deleteFarm_access_denied_failure() {
        farm.setUser(User.builder().id(2L).build());
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));

        assertThatThrownBy(() -> farmService.deleteMyFarm(user, 1L))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.FARM_ACCESS_DENIED.getMessage());
    }

    @DisplayName("타 농가를 조회한다.")
    @Test
    void findOtherFarms_success() {
        FarmSearchCond farmSearchCond = new FarmSearchCond();
        FarmSearchResponse farmSearchResponse = new FarmSearchResponse();

        when(farmRepository.findByNotUserAndFarmSearchCond(any(), any(), any()))
            .thenReturn(new PageImpl<>(singletonList(farmSearchResponse)));

        farmService.findOtherFarms(user, farmSearchCond, Pageable.unpaged());

        verify(farmRepository)
            .findByNotUserAndFarmSearchCond(user, farmSearchCond, Pageable.unpaged());
    }
}
