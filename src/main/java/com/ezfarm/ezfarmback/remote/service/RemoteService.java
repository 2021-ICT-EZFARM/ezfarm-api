package com.ezfarm.ezfarmback.remote.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.remote.domain.Remote;
import com.ezfarm.ezfarmback.remote.domain.RemoteRepository;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RemoteService {

  private final RemoteRepository remoteRepository;
  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;

  public RemoteResponse viewRemote(Long farmId) {
    Optional<Remote> remote = remoteRepository.findByFarm(farmId);
    if (remote.isPresent()) {
      return modelMapper.map(remote, RemoteResponse.class);
    }
    return new RemoteResponse();
  }

  public void updateRemote(Long farmId, RemoteRequest remoteRequest) {
    Remote remote = remoteRepository.findByFarm(farmId).orElse(Remote.builder().build());
    try {
      remote.updateRemote(objectMapper.writeValueAsString(remoteRequest));
    } catch (JsonProcessingException e) {
      new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

  }
}
