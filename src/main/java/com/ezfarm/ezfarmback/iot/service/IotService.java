package com.ezfarm.ezfarmback.iot.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.iot.IotConnector;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.iot.domain.Remote;
import com.ezfarm.ezfarmback.iot.domain.RemoteHistory;
import com.ezfarm.ezfarmback.iot.domain.RemoteHistoryRepository;
import com.ezfarm.ezfarmback.iot.domain.RemoteRepository;
import com.ezfarm.ezfarmback.iot.domain.Screen;
import com.ezfarm.ezfarmback.iot.domain.ScreenRepository;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import com.ezfarm.ezfarmback.iot.dto.RemoteResponse;
import com.ezfarm.ezfarmback.iot.dto.ScreenResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class IotService {

  private final IotConnector iotConnector;
  private final FarmRepository farmRepository;
  private final ScreenRepository screenRepository;
  private final RemoteRepository remoteRepository;
  private final RemoteHistoryRepository remoteHistoryRepository;

  public ScreenResponse findLiveScreen(User user, Long farmId) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    farm.validateIsMyFarm(user);
    String measureTime = iotConnector.getLiveScreen(farm.getId());
    return ScreenResponse.of(validateMeasureTimeAndGetScreen(farm, measureTime));
  }

  public Screen validateMeasureTimeAndGetScreen(Farm farm, String measureTime) {
    return screenRepository.findByFarmAndMeasureTime(farm, measureTime)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SCREEN_MEASURE_TIME));
  }

  @Transactional(readOnly = true)
  public RemoteResponse findRemote(Long farmId) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    Remote remote = remoteRepository.findByFarm(farm).orElseGet(
        () -> remoteRepository.save(Remote.create(farm))
    );
    return RemoteResponse.of(remote);
  }

  public Farm validateFarmIdAndGetFarm(Long farmId) {
    return farmRepository.findById(farmId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));
  }

  public void updateRemote(User user, RemoteRequest request) {
    Remote remote = validateRemoteIdAndGetRemote(request.getRemoteId());
    remote.getFarm().validateIsMyFarm(user);
    iotConnector.updateRemote(request);
    remoteHistoryRepository.saveAll(RemoteHistory.create(remote, request));
    remote.updateRemote(request);
  }

  public Remote validateRemoteIdAndGetRemote(Long remoteId) {
    return remoteRepository.findById(remoteId)
        .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
