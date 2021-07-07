package com.ezfarm.ezfarmback.remote.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.remote.domain.OnOff;
import com.ezfarm.ezfarmback.remote.domain.Remote;
import com.ezfarm.ezfarmback.remote.domain.RemoteRepository;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RemoteService {

    private final FarmRepository farmRepository;

    private final RemoteRepository remoteRepository;

    @Transactional(readOnly = true)
    public RemoteResponse findRemote(Long farmId) {
        Farm findFarm = farmRepository.findById(farmId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));

        Remote remote = remoteRepository.findByFarm(findFarm)
            .orElseGet(() -> createRemote(findFarm));

        return RemoteResponse.of(remote);
    }

    private Remote createRemote(Farm findFarm) {
        return remoteRepository.save(
            Remote.builder()
                .farm(findFarm)
                .co2(OnOff.OFF)
                .illuminance(OnOff.OFF)
                .temperature(0.0f)
                .water(OnOff.OFF)
                .build()
        );
    }

    public void updateRemote(RemoteRequest remoteRequest) {
        Remote findRemote = remoteRepository.findById(remoteRequest.getRemoteId())
            .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
        findRemote.updateRemote(remoteRequest);
    }
}
