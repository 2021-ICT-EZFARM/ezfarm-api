package com.ezfarm.ezfarmback.remote.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.remote.domain.Remote;
import com.ezfarm.ezfarmback.remote.domain.RemoteRepository;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
@DisplayName("제어 단위 테스트(Service)")
class RemoteServiceTest {

  RemoteService remoteService;

  Remote remote;

  @Mock
  RemoteRepository remoteRepository;

  RemoteResponse remoteResponse;

  @Mock
  ModelMapper modelMapper;

  String jsonString;

  @BeforeEach
  void setUp() {
    remoteService = new RemoteService(remoteRepository, modelMapper);

    jsonString = "{tmp: 30, humidity: 30, illuminance: 30, co2: 30, ph: 30, mos: 30}";

    remoteResponse = new RemoteResponse(jsonString, false);

    Farm farm = Farm.builder().build();
    remote = Remote.builder()
        .farm(farm)
        .values(jsonString)
        .successYn(false)
        .build();

  }

  @DisplayName("농장 제어 확인")
  @Test
  void viewRemote() {
    when(remoteRepository.findByFarm(any())).thenReturn(ofNullable(remote));
    when(modelMapper.map(any(), any())).thenReturn(remoteResponse);

    RemoteResponse response = remoteService.viewRemote(1L);

    assertThat(response.getValues()).isEqualTo(remote.getValues());
    assertThat(response.getSuccessYn()).isEqualTo(remote.getSuccessYn());
  }
}