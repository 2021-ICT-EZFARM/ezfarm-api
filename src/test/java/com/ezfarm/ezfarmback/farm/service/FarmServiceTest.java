package com.ezfarm.ezfarmback.farm.service;

import static java.util.Optional.*;
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
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("농장 단위 테스트(Service)")
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
            .name("남상우")
            .email("a@gmail.com")
            .password("비밀번호")
            .role(Role.ROLE_USER)
            .build();

        farmRequest = new FarmRequest(
            "경기",
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS,
            CropType.PAPRIKA,
            null
        );

        farmResponse = new FarmResponse(
            "경기",
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS,
            CropType.PAPRIKA,
            LocalDate.now()
        );

        farm = Farm.builder()
            .address("경기")
            .phoneNumber("010-2222-2222")
            .area("100")
            .farmType(FarmType.GLASS)
            .cropType(CropType.PAPRIKA)
            .isMain(false)
            .startDate(LocalDate.now())
            .build();
        farm.setCreatedDate(LocalDateTime.now());
    }

    @DisplayName("농장 시작일이 없는 농장을 생성한다.")
    @Test
    void createFarm_startDate_null_success() {
        //given
        when(modelMapper.map(any(), any())).thenReturn(farm);
        when(farmRepository.save(any())).thenReturn(farm);

        //when
        farmService.createFarm(user, farmRequest);

        //then
        assertThat(farm.getUser()).isEqualTo(user);
        verify(modelMapper).map(any(), any());
    }

    @DisplayName("농장 시작일이 농장 생성일 이후인 농장을 생성한다.")
    @Test
    void createFarm_startDate_notNull_success() {
        //given
        when(modelMapper.map(any(), any())).thenReturn(farm);
        when(farmRepository.save(any())).thenReturn(farm);
        farmRequest.setStartDate(LocalDate.now());

        //when
        farmService.createFarm(user, farmRequest);

        //then
        assertThat(farm.getUser()).isEqualTo(user);
        verify(modelMapper).map(any(), any());
    }

    @DisplayName("농장 시작일이 농장 생성일보다 과거이면 예외가 발생한다.")
    @Test
    void createFarm_failure_customException() {
        //given
        farmRequest.setStartDate(LocalDate.of(2000, 1, 1));

        //when, then
        assertThatThrownBy(() -> farmService.createFarm(user, farmRequest))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INVALID_FARM_START_DATE.getMessage());
    }

    @DisplayName("나의 모든 농장을 조회한다.")
    @Test
    void viewAllFarms_success() {
        farm.addOwner(user);
        when(farmRepository.findAllByUser(any())).thenReturn(Arrays.asList(farm));
        when(modelMapper.map(any(), any())).thenReturn(farmResponse);

        List<FarmResponse> farmResponses = farmService.viewAllFarms(user);

        assertThat(farmResponses).extracting("address").containsExactly(farm.getAddress());
    }

    @DisplayName("나의 농장이 없을 때 모두 조회하면 빈 리스트가 반환된다.")
    @Test
    void viewAllFarms_is_null_success() {
        when(farmRepository.findAllByUser(any())).thenReturn(Arrays.asList());

        List<FarmResponse> farmResponses = farmService.viewAllFarms(user);

        assertThat(farmResponses).isEmpty();
    }

    @DisplayName("나의 농장을 조회한다")
    @Test
    void viewFarm_success() {
        farm.addOwner(user);
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        when(modelMapper.map(any(), any())).thenReturn(farmResponse);

        FarmResponse actualResponse = farmService.viewFarm(user, 1L);

        Assertions.assertAll(
            () -> assertThat(actualResponse.getAddress()).isEqualTo(farm.getAddress()),
            () -> assertThat(actualResponse.getPhoneNumber()).isEqualTo(farm.getPhoneNumber())
        );
    }

    @DisplayName("나의 농장이 아닌경우 예외가 발생한다")
    @Test
    void viewFarm_failure_is_not_owner(@Mock User user1, @Mock User user2) {
        when(user1.getId()).thenReturn(1L);
        when(user2.getId()).thenReturn(2L);
        farm.addOwner(user2);
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));

        assertThatThrownBy(() -> farmService.viewFarm(user1, 1L))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.NOT_FARM_OWNER.getMessage());
    }

    @DisplayName("농장 번호가 맞지않거나 농장이 없으면 예외가 발생한다")
    @Test
    void viewFarm_failure_invalid_id() {
        when(farmRepository.findById(any())).thenThrow(CustomException.class);

        assertThatThrownBy(() -> farmService.viewFarm(user, 2L))
            .isInstanceOf(CustomException.class);
    }

    @DisplayName("나의 농장을 수정한다.")
    @Test
    void updateFarm_success() {
        farm.addOwner(user);
        FarmRequest farmRequest2 = new FarmRequest(
            "부산",
            "010-3333-3333",
            "200",
            true,
            FarmType.VINYL,
            CropType.TOMATO,
            LocalDate.now().plusDays(1)
        );
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));

        farmService.updateFarm(user, 1L, farmRequest2);

        Assertions.assertAll(
            () -> assertThat(farm.getAddress()).isEqualTo(farmRequest2.getAddress()),
            () -> assertThat(farm.getPhoneNumber()).isEqualTo(farmRequest2.getPhoneNumber()),
            () -> assertThat(farm.isMain()).isEqualTo(farmRequest2.isMain()),
            () -> assertThat(farm.getFarmType()).isEqualTo(farmRequest2.getFarmType()),
            () -> assertThat(farm.getCropType()).isEqualTo(farmRequest2.getCropType())
        );
    }

    @DisplayName("새로운 메인 농장으로 수정한다.")
    @Test
    void updateFarm_success_change_main() {
        Farm farm2 = Farm.builder()
            .address("경기")
            .phoneNumber("010-2222-2222")
            .area("100")
            .farmType(FarmType.GLASS)
            .cropType(CropType.PAPRIKA)
            .isMain(true)
            .startDate(LocalDate.now())
            .build();
        farm2.setCreatedDate(LocalDateTime.now());
        farm2.addOwner(user);
        farm.addOwner(user);

        FarmRequest farmRequest2 = new FarmRequest(
            "부산",
            "010-3333-3333",
            "200",
            true,
            FarmType.VINYL,
            CropType.TOMATO,
            LocalDate.now().plusDays(1)
        );
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        when(farmRepository.findByIsMainAndUser(anyBoolean(), any())).thenReturn(of(farm2));

        farmService.updateFarm(user, 1L, farmRequest2);

        Assertions.assertAll(
            () -> assertThat(farm.isMain()).isEqualTo(farmRequest2.isMain()),
            () -> assertThat(farm2.isMain()).isEqualTo(false)
        );
    }

    @DisplayName("농장 재배 시작 일자가 농장 생성 일자보다 빠르면 예외가 발생한다.")
    @Test
    void updateFarm_failure_startDateException() {
        farm.addOwner(user);

        FarmRequest farmRequest2 = new FarmRequest(
            "부산",
            "010-3333-3333",
            "200",
            true,
            FarmType.VINYL,
            CropType.TOMATO,
            LocalDate.now().minusDays(1)
        );

        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));

        assertThatThrownBy(() -> farmService.updateFarm(user, 1L, farmRequest2))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INVALID_FARM_START_DATE.getMessage());
    }

    @DisplayName("내 농가를 삭제한다.")
    @Test
    void deleteFarm() {
        farm.addOwner(user);
        when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
        farmService.deleteFarm(user, 1L);

        verify(farmRepository).delete(farm);
    }
}
