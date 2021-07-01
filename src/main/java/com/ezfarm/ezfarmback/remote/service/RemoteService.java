package com.ezfarm.ezfarmback.remote.service;

import com.ezfarm.ezfarmback.remote.domain.Remote;
import com.ezfarm.ezfarmback.remote.domain.RemoteRepository;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
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

  public RemoteResponse viewRemote(Long farmId) {
    Optional<Remote> remote = remoteRepository.findByFarm(farmId);
    if (remote.isPresent()) {
      return modelMapper.map(remote, RemoteResponse.class);
    }
    return new RemoteResponse();
  }
}
